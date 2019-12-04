package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class Config_3344_StartSpringCloudApp {
    public static void main(String[] args) {
        SpringApplication.run(Config_3344_StartSpringCloudApp.class, args);
    }

}
/*
三种访问GitHub远程库配置文件的方式
http://config-3344.com:3344/application-dev.yml
http://config-3344.com:3344/application/dev/master 分支
http://config-3344.com:3344/master/application-dev.yml
 */