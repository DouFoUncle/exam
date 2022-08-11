package com.project.commons.config;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 异常信息类
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
@Configuration
public class MyErrorConfig {

    /**
     * 以编程方式配置嵌入式servlet容器，可以通过注册实现该 WebServerFactoryCustomizer 接口的Spring bean
     * TomcatServletWebServerFactory，JettyServletWebServerFactory并且UndertowServletWebServerFactory 是专用变体，
     ConfigurableServletWebServerFactory分别为Tomcat，Jetty和Undertow提供了额外的自定义setter方法。
     * @return
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                // 对嵌入式servlet容器的配置
                // factory.setPort(8081);
                /* 注意：new ErrorPage(stat, path);中path必须是页面名称，并且必须“/”开始。
                    底层调用了String.java中如下方法：
                    public boolean startsWith(String prefix) {
                        return startsWith(prefix, 0);
                    }*/
                ErrorPage errorPage403 = new ErrorPage(HttpStatus.FORBIDDEN,
                        "/error/error-403");
                ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND,
                        "/error/error-404");
                ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,
                        "/error/error-500");
                factory.addErrorPages(errorPage403, errorPage404,
                        errorPage500);
            }
        };
    }
}
