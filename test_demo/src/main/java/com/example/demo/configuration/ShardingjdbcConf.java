package com.example.demo.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.demo.shardingjdbc.sharding.MyPreciseShardingAlgorithm;
import com.github.pagehelper.PageHelper;
import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingjdbc.core.api.config.strategy.StandardShardingStrategyConfiguration;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shardingjdbc的配置文件
 */
//@Configuration
public class ShardingjdbcConf {

    //@Bean
    TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        //配置逻辑表名，并非数据库中真实存在的表名，而是sql中使用的那个，不受分片策略而改变.
        //例如：select * frpm t_order where user_id = xxx
        orderTableRuleConfig.setLogicTable("t_order");
        //配置真实的数据节点，即数据库中真实存在的节点，由数据源名 + 表名组成
        //${} 是一个groovy表达式，[]表示枚举，{...}表示一个范围。
        //整个inline表达式最终会是一个笛卡尔积，表示ds_0.t_order_0. ds_0.t_order_1
        // ds_1.t_order_0. ds_1.t_order_0
        orderTableRuleConfig.setActualDataNodes("world.t_order_${0..2},xmall.t_order_${3..4}");
        //主键生成列，默认的主键生成算法是snowflake
        orderTableRuleConfig.setKeyGeneratorColumnName("order_id");
        orderTableRuleConfig.setKeyGeneratorClass(io.shardingjdbc.core.keygen.DefaultKeyGenerator.class.getName());
        //设置分片策略，这里简单起见直接取模，也可以使用自定义算法来实现分片规则
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order_${order_id % 5}"));
        return orderTableRuleConfig;
    }

    //@Bean
    TableRuleConfiguration getOrderItemTableRuleConfiguration() {
        TableRuleConfiguration orderItemTableRuleConfig = new TableRuleConfiguration();
        orderItemTableRuleConfig.setLogicTable("t_order_item");
        orderItemTableRuleConfig.setActualDataNodes("world.t_order_${0..1},xmall.t_order_${2..4}");
        orderItemTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_item_id", "t_order_item_${order_id % 5}"));
        return orderItemTableRuleConfig;
    }

    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>(2, 1);
        result.put("world", createDataSource("world"));
        result.put("xmall", createDataSource("xmall"));
        return result;
    }

    private DataSource createDataSource(final String dataSourceName) {
        DruidDataSource result = new DruidDataSource();
        result.setInitialSize(10);
        result.setMinIdle(10);
        result.setMaxActive(50);
        result.setDriverClassName("com.mysql.jdbc.Driver");
        result.setUrl(String.format("jdbc:mysql://localhost:3306/%s?useSSL=false", dataSourceName));
        result.setUsername("root");
        result.setPassword("root");
        return result;
    }

    /**
     * Sharding-JDBC采用snowflake算法作为默认的分布式分布式自增主键策略，用于保证分布式的情况下可以无中心化的生成不重复的自增序列。
     * 因此自增主键可以保证递增，但无法保证连续。而snowflake算法的最后4位是在同一毫秒内的访问递增值。因此，如果毫秒内并发度不高，最后4位为零的几率则很大。因此并发度不高的应用生成偶数主键的几率会更高。
     *
     * @return
     * @throws SQLException
     */
    //@Bean
    DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
        shardingRuleConfig.getTableRuleConfigs().add(getOrderItemTableRuleConfiguration());
        shardingRuleConfig.getBindingTableGroups().add("t_order, t_order_item");
        // 自定义的分片算法实现
        StandardShardingStrategyConfiguration standardStrategy = new StandardShardingStrategyConfiguration("order_id", MyPreciseShardingAlgorithm.class.getName());

        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(standardStrategy);
        //shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "ds_${user_id % 5}"));
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order_${order_id % 5}"));
        shardingRuleConfig.setDefaultKeyGeneratorClass(io.shardingjdbc.core.keygen.DefaultKeyGenerator.class.getName());//全局主键生成器
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new ConcurrentHashMap<>(), new Properties());
    }

    //@Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(getShardingDataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:/mybatis/mybatis-config.xml"));
        sqlSessionFactoryBean.setPlugins(getInterceptor());
        return sqlSessionFactoryBean.getObject();
    }

    //@Bean
    public Interceptor[] getInterceptor() {
        Interceptor interceptor = new PageHelper();
        Properties props = new Properties();
        props.setProperty("dialect", "mysql");
        props.setProperty("offsetAsPageNum", "false");
        props.setProperty("pageSizeZero", "true");
        props.setProperty("reasonable", "false");
        props.setProperty("supportMethodsArguments", "false");
        props.setProperty("returnPageInfo", "none");
        interceptor.setProperties(props);
        Interceptor[] interceptors = {interceptor};
        return interceptors;
    }

    //@Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        return new DataSourceTransactionManager(getShardingDataSource());
    }

}

