package com.example.demo.cache.controller;

import com.example.demo.cache.entity.Student;
import com.example.demo.cache.service.StudentService;
import org.redisson.spring.cache.RedissonCache;
import org.redisson.spring.cache.RedissonCacheStatisticsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.cache.CacheStatistics;
import org.springframework.cache.Cache;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;


/**
 * localhost:8080/testGuava
 */
@RestController
public class GuavaCacheController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CompositeCacheManager compositeCacheManager;

    @Autowired
    private RedissonCacheStatisticsProvider redissonCacheStatisticsProvider;

    /**
     * 测试不同缓存 使用 CompositeCacheManager 可以容纳所有的 CacheManager 只需要取对应名字的 Cache 就可以了
     * Guava Cache
     * Caffeine Cache
     * Redisson Cache
     * EhCache Cache
     *
     * 总结 : 每种缓存的统计功能
     * Guava Cache / Caffeine Cache 自带统计功能,在创建缓存是通过建造者模式指定开启,通过包装后的Cache.stats()进行调用获取
     * EhCache Cache 直接在包装后的Cache中进行显示
     * Redisson Cache 比较特殊,统计数据的包装前的RedissonCache中,可以通过SpringBoot中的监控包RedissonCacheStatisticsProvider进行统计(数据不是很全,也可以自行统计)
     *
     * ps:SpringBoot监控包封装了CacheStatisticsProvider接口,提供了缓存监控的基础方法,也可以进行扩展
     */
    @RequestMapping("/testGuava")
    public Object testGuava() {

        Integer id = 1;
        for (int i = 0; i < 30; i++) {
            Student stu = studentService.getStudent(i);  //新建缓存
        }
        Student stu = studentService.getStudent(id);  //新建缓存
        stu = studentService.getStudent(id);   //从缓存中取
        stu.setName("ABC");
        studentService.updateStudent(stu);


        Collection<String> cacheNames = compositeCacheManager.getCacheNames();
        /**
         *
         */
        for (String cacheName : cacheNames) {
            System.out.print(cacheName + " ---- ");
            Cache oldCache = compositeCacheManager.getCache(cacheName);//org.redisson.spring.cache.RedissonCache 包装前的有统计功能
            if (oldCache instanceof RedissonCache) {
                CacheStatistics cacheStatistics = redissonCacheStatisticsProvider.getCacheStatistics(compositeCacheManager, (RedissonCache) oldCache);
                System.out.println("RedissonCache 统计数据 : " + cacheStatistics.getSize() + " -- " + cacheStatistics.getHitRatio() + " -- " + cacheStatistics.getMissRatio());
            }
            Object cache = compositeCacheManager.getCache(cacheName).getNativeCache();
            if (cache instanceof com.github.benmanes.caffeine.cache.Cache) {
                com.github.benmanes.caffeine.cache.Cache caffeineCache = (com.github.benmanes.caffeine.cache.Cache) cache;
                System.out.println("caffeineCache 统计数据 :" + caffeineCache.stats());
                ConcurrentMap concurrentMap = caffeineCache.asMap();
                for (Object o : concurrentMap.keySet()) {
                    System.out.println("Key : " + o + " --- Value : " + concurrentMap.get(o));
                }
            }
            System.out.println(cache);
        }



//        Cache redissonCache = compositeCacheManager.getCache("redissonCache");
//        Cache.ValueWrapper valueWrapper = redissonCache.get("id_1");
//        System.out.println("redisson 1 : " + valueWrapper.get());
//        Cache guavaCache = compositeCacheManager.getCache("guavaCache");
//        Cache.ValueWrapper wrapper = guavaCache.get("id_1");
//        System.out.println("guava 1 : " + wrapper.get());
//
//        valueWrapper = redissonCache.get("id_1");
//        System.out.println("redisson 2 : " + valueWrapper.get());


//        studentService.myDelete(id);
//        stu = studentService.getStudent(id);   //从缓存中取
//        System.out.println("STU2 :" + stu);
//
//        stu.setName("banana");  //重新设置值
//        studentService.updateStudent(stu); //更新缓存
//        stu = studentService.getStudent(id); //从缓存中取出新值
//
//        stu = new Student();  //新实例
//        stu.setId(0);
//        studentService.updateStudent(stu);  //用新建的实例进行更新，会新建缓存
//        stu = studentService.getStudent(0);  //从缓存中取
//
//        studentService.deleteStudent(id);  // 删除缓存
//        stu = studentService.getStudent(id);  //再次新建缓存
//
//        id = 2;
//        stu = studentService.getStudent(id); //新建缓存
//        studentService.deleteAllStudent(); //删除所有缓存
//        id = 1;
//        stu = studentService.getStudent(id); //因所有缓存被前一步清除，会新建缓存
//
//        id = 5;
//        stu = studentService.getStudent(id); //不会新建缓存 因为设置了缓存条件必须小于3
//        stu = studentService.getStudent(id); //因没有缓存，不会从缓存中取

        return "OK";
    }
}




