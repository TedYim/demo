package com.example.demo.filter;

import com.google.common.hash.Funnels;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分布式布隆过滤器
 * 测试结果:
 *  分布式:
 *      成功过滤到999998
 *      布隆过滤器消耗时间:70040500
 *      错判率为:314
 *  单机:
 *      成功过滤到999998
 *      布隆过滤器消耗时间:423100
 *      错判率为:309
 */
@RestController
public class RedisBloomFilterDemo {

    private static final int CAPACITY = 1000000;//容量

    private static final double FPP = 0.03;//容错率

    private static final BloomFilterHelper<Integer> BLOOM_FILTER_HELPER = new BloomFilterHelper<>(Funnels.integerFunnel(), CAPACITY, FPP);//创建一个分布式布隆过滤器

    private static final String BLOOM_FILTER_NAME = "REDIS_BLOOM_FILTER_KEY";//分布式布隆过滤器命名

    private static final int TEST_KEY = 999998;

    private final RedisService redisService;

    public RedisBloomFilterDemo(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 初始化布隆过滤器
     */
    public void initBloomFilter() {
        for (int i = 0; i < CAPACITY; i++) {
            redisService.addByBloomFilter(BLOOM_FILTER_HELPER, BLOOM_FILTER_NAME, i);
        }
    }

    @GetMapping("/bloomFilter")
    public void bloomFilterTest() {
        //initBloomFilter();

        /*返回计算机最精确的时间，单位微妙*/
        long start = System.nanoTime();

        if (redisService.includeByBloomFilter(BLOOM_FILTER_HELPER, BLOOM_FILTER_NAME, TEST_KEY)) {
            System.out.println("成功过滤到" + TEST_KEY);
        }
        long end = System.nanoTime();
        System.out.println("布隆过滤器消耗时间:" + (end - start));

        int sum = 0;

        for (int i = CAPACITY + 20000; i < CAPACITY + 30000; i++) {
            if (redisService.includeByBloomFilter(BLOOM_FILTER_HELPER, BLOOM_FILTER_NAME, i)) {
                sum = sum + 1;
            }
        }
        System.out.println("错判率为:" + sum);
    }

}
