package com.project.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc相关配置
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 设置首页的跳转
     * 将该方法返回的WebMvcConfigurer添加进Spring容器
     * @return
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        WebMvcConfigurer webMvcConfigurer = new WebMvcConfigurer() {

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // 配置不拦截静态资源
                registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
            }

            /**
             * 扩展添加视图映射
             * @param registry
             */
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("login");   //拦截 main.html
            }

            /**
             * 添加拦截器
             * @param registry
             */
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(getLoginInterceptor())
                        .addPathPatterns("/**")     // 下面配置忽略拦截哪些
                        .excludePathPatterns("/", "/admin", "/static/**", "/component/**", "/adminStatic/**","/config/**","/upload/**", "/util/**"
                                , "/css/**", "/js/**", "/lib/**", "/images/**", "/fonts/**", "/upload/**", "/doc.html/**", "/error/**", "/favicon.ico"
                                , "/system/toTimeOutPage", "/system/logout", "/system/"
                                , "/public/**", "/findIcon", "/system/icon", "/login/**");
            }
        };
        return webMvcConfigurer;
    }

    @Bean
    public LoginInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }

}