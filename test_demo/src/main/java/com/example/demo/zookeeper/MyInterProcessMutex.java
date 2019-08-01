package com.example.demo.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.Reaper;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ThreadUtils;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class MyInterProcessMutex extends InterProcessMutex {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Reaper reaper;

    private CuratorFramework client;

    private String path;

    private static final int DEFAULT_REAPING_THRESHOLD_MS = 30000;


    public MyInterProcessMutex(CuratorFramework client, String path) {
        this(client, path, DEFAULT_REAPING_THRESHOLD_MS);
    }


    public MyInterProcessMutex(CuratorFramework client, String path, int reapingThresholdMs) {
        super(client, path);
        this.client = client;
        this.path = path;
        this.reaper = new Reaper(client, MyInterProcessMutexThreadPool.getInstance(),
                reapingThresholdMs != 0 ? reapingThresholdMs : DEFAULT_REAPING_THRESHOLD_MS, (String) null);
        try {
            reaper.start();
            log.debug("reaper start ...");
        } catch (Exception ignored) {
        }
    }


    @Override
    public void acquire() throws Exception {
        super.acquire();
        this.reaper.addPath(this.path, Reaper.Mode.REAP_UNTIL_DELETE);
        log.debug("reaper add path : [ {} ]", this.path);
    }


    @Override
    public void release() throws Exception {
        super.release();
        for (; ; ) {
            TimeUnit.SECONDS.sleep(10);
            Stat stat = client.checkExists().forPath(path);
            if (stat == null) {
                reaper.removePath(path);
                log.debug("reaper remove path : [ {} ]", this.path);
                CloseableUtils.closeQuietly(reaper);
                log.debug("reaper close ...");
                break;
            }
        }
    }

    static class MyInterProcessMutexThreadPool {

        private MyInterProcessMutexThreadPool() {
        }

        private static final ScheduledExecutorService instance = ThreadUtils.newSingleThreadScheduledExecutor("MyReaper");

        public static ScheduledExecutorService getInstance() {
            return instance;
        }
    }
}
