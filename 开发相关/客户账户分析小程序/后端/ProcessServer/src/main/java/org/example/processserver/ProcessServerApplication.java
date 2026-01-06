package org.example.processserver;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class})
@EnableScheduling
public class ProcessServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessServerApplication.class, args);
    }

}
