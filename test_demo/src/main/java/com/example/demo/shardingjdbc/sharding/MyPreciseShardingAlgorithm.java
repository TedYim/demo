package com.example.demo.shardingjdbc.sharding;

import com.alibaba.fastjson.JSON;
import io.shardingjdbc.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingjdbc.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * 自定义分片规则
 * 泛型代表需要分片值的类型
 */
@Slf4j
public class MyPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
    /**
     * 自定义分片规则
     *
     * @param availableTargetNames 代表可用的数据库 / 或者分片的表
     * @param shardingValue        代表需要被分片的键 值
     * @retuen 返回写入哪个库 / 表即可
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        //collection:["world","xmall"],preciseShardingValue:{"columnName":"user_id","logicTableName":"t_order","value":0}
        log.info("collection:" + JSON.toJSONString(availableTargetNames) + ",preciseShardingValue:" + JSON.toJSONString(shardingValue));
        Long value = shardingValue.getValue();//order_id的值
        if (value % 5 == 3 || value % 5 == 4) {
            return "xmall"; //偶数在world
        } else {
            return "world";//奇数在xmall
        }
    }
}
