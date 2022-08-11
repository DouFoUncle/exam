package com.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.project.commons.mapper")
public class ExamAdminApplication {

    public static void main(String[] args) {
        // 热部署
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(ExamAdminApplication.class, args);
    }

}
