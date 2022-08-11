package com.project.commons.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 配置MyBatisPlus
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
@Configuration
@EnableTransactionManagement
public class MyBatisPlusConfig {

    /**
     * 注入分页插件
     * @return
     */
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor page = new PaginationInnerInterceptor();
        page.setDbType(DbType.MYSQL);
        return page;
    }

    /**
     * 配置分页插件
     * 需要注意的是pom.xml文件中不要再引入MyBatis的分页插件
     * 不要引入MyBatis的分页插件：PageHelper ！！
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        return interceptor;
    }
}
