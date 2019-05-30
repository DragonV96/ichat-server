package com.ichat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author glw
 * @date 2018/11/14 13:23
 */
@SpringBootApplication
@MapperScan(basePackages = "com.ichat.mapper")  // 扫描mybatis mapper包路径
@ComponentScan(basePackages = {"com.ichat", "org.n3r.idworker"})    // 扫描所有需要的包，包含一些自用的工具类包所在的路径
public class Application {

    @Bean
    public SpringUtil getSpringUtil(){
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
