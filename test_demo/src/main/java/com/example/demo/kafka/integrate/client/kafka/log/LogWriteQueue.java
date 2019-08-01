package com.example.demo.kafka.integrate.client.kafka.log;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public final class LogWriteQueue implements LogConstant {
    static final   Logger        log            = LoggerFactory.getLogger(LogWriteQueue.class);
    private static int           INSTANCE_COUNT = 3;
    static         LogWriteQueue logQueue       = new LogWriteQueue();
    List<LogTaskQueue> queues = new ArrayList<LogTaskQueue>();

    private LogWriteQueue() {
        //初始化日志模块
        for (int i = 0; i < INSTANCE_COUNT; i++) {
            queues.add(new LogTaskQueue());
        }
        File file = new File(logDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private class LogTaskQueue implements Runnable {
        private Vector<LogModel> vector = new Vector<LogModel>(100);

        public LogTaskQueue() {
            new Thread(this).start();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (vector.isEmpty()) {
                        Thread.sleep(1000);
                        continue;
                    }
                    LogModel model = vector.remove(0);
                    File file = new File(logDir, model.messageId + segmentation + model.getTopic() + segmentation + model.getOperationEnum().name() + segmentation + model.getIp());
                    FileOutputStream output = new FileOutputStream(file);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("messageBody", model.messageBody);
                    jsonObject.put("keyword", model.keyword);
                    jsonObject.put("keyword2", model.keyword2);
                    jsonObject.put("recordTime", model.getRecordTime());
                    IOUtils.write(jsonObject.toString(), output, "UTF-8");//写日志
                    IOUtils.closeQuietly(output);
                } catch (Exception e) {
                    log.error("LogQueueTask errory", e);
                }
            }
        }
    }

    public static void writeLog(String topic, String messageId, String messageBody, MsgTypeEnum typeEnum) {
        writeLog(topic, messageId, messageBody, typeEnum, "", "");
    }

    public static void writeLog(String topic, String messageId, String messageBody, MsgTypeEnum typeEnum, String keyword1, String keyword2) {
        LogModel model = new LogModel(topic, messageId, messageBody, typeEnum);
        model.setIp(getLoacalHost());
        model.setRecordTime(System.currentTimeMillis());
        model.setKeyword(keyword1);
        model.setKeyword2(keyword2);
        int r = new Random().nextInt(INSTANCE_COUNT);
        logQueue.queues.get(r).vector.add(model);
    }

    public static String getLoacalHost() {
        String host = "";
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return host;
    }
}
