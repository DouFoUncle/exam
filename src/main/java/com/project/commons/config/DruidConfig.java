package com.project.commons.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Druid的配置类
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource getDruidDataSource(){
        return new DruidDataSource();
    }

}
