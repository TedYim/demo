//package com.example.demo.poi;
//
//import com.google.common.collect.Lists;
//import com.topscore.mapper.StoreStockMapper;
//import com.topscore.service.stock.BaseTest;
//import com.topscore.service.stock.store.pojo.dto.StoreStockDto;
//import com.topscore.service.stock.store.pojo.vo.StoreStockVo;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.io.FileOutputStream;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class POITest extends BaseTest {
//
//    @Autowired
//    private StoreStockMapper storeStockMapper;
//
//    private Integer threadNum = Runtime.getRuntime().availableProcessors() * 2;
//
//    @Test
//    public void testPOI() throws Exception {
//        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
//
//        final int total = 100_0000;//支持百万级别
//        int pageSize = 1000;//每次1000
//        CountDownLatch countDownLatch = new CountDownLatch(total / pageSize);
//
//        List<StoreStockVo> all = Lists.newArrayList();
//
//        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < total / pageSize; i++) {
//            StoreStockDto storeStockDto = new StoreStockDto();
//            storeStockDto.setPageSize(pageSize);
//            storeStockDto.setCurrentPage(i);
//            int finalI = i;
//            executorService.execute(() -> {
//                List<StoreStockVo> storeStockVos = storeStockMapper.queryStoreStockByCondition(storeStockDto);
//                all.addAll(storeStockVos);
//                countDownLatch.countDown();
//                System.err.println("已完成任务 : " + finalI);
//            });
//        }
//        countDownLatch.await();
//        long endTime = System.currentTimeMillis();
//        System.out.println("process [" + all.size() + "] spent time: [" + (endTime - startTime) + "]");
//        //prcoessCSV(66666);
//    }
//
//    private static void prcoessCSV(int rowsNum) throws Exception {
//        try {
//            long startTime = System.currentTimeMillis();
//            final int NUM_OF_ROWS = rowsNum;
//            final int NUM_OF_COLUMNS = 30;
//
//            SXSSFWorkbook wb = null;
//            try {
//                wb = new SXSSFWorkbook();
//                wb.setCompressTempFiles(true); //压缩临时文件，很重要，否则磁盘很快就会被写满
//                Sheet sh = wb.createSheet();
//                int rowNum = 0;
//                for (int num = 0; num < NUM_OF_ROWS; num++) {
//                    if (num % 100_0000 == 0) {
//                        sh = wb.createSheet("sheet " + num);
//                        rowNum = 0;
//                    }
//                    rowNum++;
//                    Row row = sh.createRow(rowNum);
//                    for (int cellnum = 0; cellnum < NUM_OF_COLUMNS; cellnum++) {
//                        Cell cell = row.createCell(cellnum);
//                        cell.setCellValue(Math.random());
//                    }
//                }
//
//                FileOutputStream out = new FileOutputStream("ooxml-scatter-chart_SXSSFW_" + rowsNum + ".xlsx");
//                wb.write(out);
//                out.close();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            } finally {
//                if (wb != null) {
//                    wb.dispose();// 删除临时文件，很重要，否则磁盘可能会被写满
//                }
//            }
//
//            long endTime = System.currentTimeMillis();
//            System.out.println("process " + rowsNum + " spent time:" + (endTime - startTime));
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//
//    }
//
//
//}
