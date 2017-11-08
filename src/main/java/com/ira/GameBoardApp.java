package com.ira;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
@EnableSwagger2
public class GameBoardApp {

    private static final Logger logger = LoggerFactory.getLogger(GameBoardApp.class);

    public static final String regex =  "[-+%. &//\'@^:,;*{}/[/]]";
    public static final String r ="_";

    public static void main(String[] args) {

        logger.info(" Started main");

        SpringApplication.run(GameBoardApp.class, args);
    }
}

