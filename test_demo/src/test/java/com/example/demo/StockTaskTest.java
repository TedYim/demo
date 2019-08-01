package com.example.demo;

import com.example.demo.util.HttpClientUtil;
import com.google.common.collect.Lists;

import java.util.ArrayList;

public class StockTaskTest {

    static String clearSyncLogJob            = "http://10.10.0.31:9015/stock-interface/api/stock/online/clearSyncLog";
    static String syncStockProductInfo       = "http://10.10.0.31:9015/stock-interface/api/stock/online/syncStockProductInfo";
    static String handleStockRollbackFailLog = "http://10.10.0.31:9015/stock-interface/api/stock/online/handleStockRollbackFailLog";
    static String handleExpireSettingJob     = "http://10.10.0.31:9015/stock-interface/api/hana/jobInvoke?task=handleExpireSetting";
    static String handleWaitingRecord        = "http://10.10.0.31:9015/stock-interface/api/hana/jobInvoke?task=handleWaitingRecord";
    static String handleSourceAgain          = "http://10.10.0.31:9015/stock-interface/api/hana/jobInvoke?task=handleSourceAgain";

    public static void main(String[] args) throws Exception {
        ArrayList<String> urls = Lists.newArrayList(clearSyncLogJob, syncStockProductInfo, handleStockRollbackFailLog, handleExpireSettingJob, handleWaitingRecord, handleSourceAgain);
        while (true) {
            for (String url : urls) {
                String result = HttpClientUtil.doGet(url);
                System.err.println(result);
                Thread.sleep(100L);
            }
            Thread.sleep(1000L);
        }
    }
}
