package com.spy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author spy
 * @create 2023-02-27 11:28
 */
@SpringBootApplication
@Slf4j
@ServletComponentScan
@EnableTransactionManagement
public class TakeawayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TakeawayApplication.class);
        log.info("项目启动成功");
    }
}
