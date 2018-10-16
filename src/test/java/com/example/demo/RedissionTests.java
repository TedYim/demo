package com.example.demo;

import com.example.demo.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedissionTests {

    @Autowired
    private RedissonClient redisson;

    @Test
    public void redissonClientTest() throws Exception {
        RLock fairLock = this.redisson.getLock("Lock");
        System.err.println(fairLock.isLocked());
        fairLock.lock();
        System.err.println(fairLock);
        System.err.println(this.redisson.getConfig().toJSON());
    }


    /**
     * 测试加锁时间 : 本地单机8000个锁13s
     *
     * @throws Exception
     */
    @Test
    public void lockTimeTest() throws Exception {
        List<RLock> locks = new LinkedList<>();
        for (int i = 0; i <= 8; i++) {
            locks.add(redisson.getLock("lock" + i));
        }

        RLock lock1 = redisson.getLock("lock1");
        RLock lock2 = redisson.getLock("lock2");
        //RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2);
        //RedissonRedLock lock = new RedissonRedLock(locks.toArray(new RLock[locks.size()]));
        RedissonRedLock lock = new RedissonRedLock(lock1, lock2);
        // 同时加锁：lock1 lock2 lock3
        // 所有的锁都上锁成功才算成功。
        System.err.println("加锁前时间 : " + DateUtil.getCurrentTime());
        lock.lock();
        System.err.println("加锁成功时间 : " + DateUtil.getCurrentTime());
        Thread.sleep(2 * 60 * 1000);
        lock.unlock();
        System.err.println("去除锁成功");
        //Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void concurrentTest() throws Exception {

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.err.println("线程1启动时间 : " + DateUtil.getCurrentTime());
                List<RLock> locks = new LinkedList<>();
                for (int i = 0; i <= 8000; i++) {
                    locks.add(redisson.getLock("lock" + i));
                }
                RLock[] lockArray = locks.toArray(new RLock[locks.size()]);
                RedissonRedLock lock = new RedissonRedLock(lockArray);
                Long currentTime = System.currentTimeMillis();
                System.err.println("线程1 - lock前时间 : " + DateUtil.getCurrentTime());
                lock.lock();
                System.err.println("线程1 - lock后时间 : " + DateUtil.getCurrentTime() + "开始处理业务 , 时间1分钟");
                System.err.println("线程1 - 加锁时间 : " + (System.currentTimeMillis() - currentTime) + "ms");
                try {
                    Thread.sleep(60 * 1000);//休眠1分钟
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Long currentAfterTime = System.currentTimeMillis();
                System.err.println("线程1 - 准备解锁lock时间 : " + DateUtil.getCurrentTime() + " 处理业务结束");
                lock.unlock();
                System.err.println("线程1 - 解锁lock完成时间 : " + DateUtil.getCurrentTime());
                System.err.println("线程1 - 解锁时间 : " + (System.currentTimeMillis() - currentAfterTime) + "ms");
            }
        }).start();

        Thread.sleep(5 * 1000);//休眠5s

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.err.println("线程2启动时间 : " + DateUtil.getCurrentTime());
                List<RLock> locks = new LinkedList<>();
                for (int i = 1000; i <= 9000; i++) {
                    locks.add(redisson.getLock("lock" + i));
                }
                RLock[] lockArray = locks.toArray(new RLock[locks.size()]);
                RedissonRedLock lock = new RedissonRedLock(lockArray);
                Long currentTime = System.currentTimeMillis();
                System.err.println("线程2 - lock前时间 : " + DateUtil.getCurrentTime());
                lock.lock();
                System.err.println("线程2 - lock后时间 : " + DateUtil.getCurrentTime() + "开始处理业务 , 时间1分钟");
                System.err.println("线程2 - 加锁时间 : " + (System.currentTimeMillis() - currentTime) + "ms");
                try {
                    Thread.sleep(60 * 1000);//休眠1分钟
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Long currentAfterTime = System.currentTimeMillis();
                System.err.println("线程2 - 准备解锁lock时间 : " + DateUtil.getCurrentTime() + " 处理业务结束");
                lock.unlock();
                System.err.println("线程2 - 解锁lock完成时间 : " + DateUtil.getCurrentTime());
                System.err.println("线程2 - 解锁时间 : " + (System.currentTimeMillis() - currentAfterTime) + "ms");
            }
        }).start();


        Thread.sleep(Integer.MAX_VALUE);
    }


    @Test
    public void InterMultiThreadTest() throws InterruptedException {

        for (int i = 0; i < 500; i++) {
            Thread thread = new Thread(new TaskThread(new Random().nextInt(list.size())));
            thread.setName("task=" + i);
            thread.start();
            Thread.sleep(new Random().nextInt(10));//休眠
        }
        Thread.sleep(Integer.MAX_VALUE);//休眠

    }

    List<String> list = new ArrayList<String>();

    {
        for (int i = 0; i < 2000; i++) {
            list.add("lock" + i);
        }
    }

    class TaskThread implements Runnable {

        int count = 0;

        public TaskThread(int count) {
            this.count = count;
        }

        @Override
        public void run() {
            List<RLock> lockList = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                int index = new Random().nextInt(list.size());
                String lockPath = list.get(index);
                if (!lockList.contains(lockPath)) {
                    lockList.add(redisson.getLock(lockPath));
                }
            }
            System.err.println("线程" + Thread.currentThread().getName() + " 启动时间 : " + DateUtil.getCurrentTime());
            //初始化锁
            RLock[] lockArray = lockList.toArray(new RLock[lockList.size()]);
            RedissonRedLock lock = new RedissonRedLock(lockArray);
            System.err.println("线程" + Thread.currentThread().getName() + " 初始化锁数量 : " + lockList.size());
            try {
                Long currentTime = System.currentTimeMillis();
                System.err.println("线程 " + Thread.currentThread().getName() + " lock前时间 : " + DateUtil.getCurrentTime());
                lock.lock();
                System.err.println("线程" + Thread.currentThread().getName() + " lock后时间 : " + DateUtil.getCurrentTime());
                System.err.println("线程" + Thread.currentThread().getName() + " 加锁时间 : " + (System.currentTimeMillis() - currentTime) + "ms");
                Thread.sleep(60 * 1000);//休眠1分钟
                lock.unlock();
                System.err.println("线程" + Thread.currentThread().getName() + "释放锁 : ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
