package com.atguigu.springcloud.cfgbeans;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RetryRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/*
    SpringBoot优化了spring的配置文件
    将spring的 applicationContext.xml 简化成注解版的@Configuration配置
    加了@Configuration改注解的类等同于以前Spring的applicationContext.xml文件
    原来applicationContext.xml文件中要加的 <bean id="userService"   class="com.atguigu.tmall.UserServiceImpl"/>
    变成
    @Bean
    public UserService getUserService(){
        return new UserServiceImpl();
    }

    其中@Bean相当于原来 xml文件中的 <bean/> 节点
    id 相当于返回值方法返回值类型  class相当于返回值

 */
@Configuration
public class ConfigBean {

    @Bean
    @LoadBalanced // SrpingCloud Ribbon 是基于 Netflix
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    /**
     * 运用该方式可以显式的指定本次负载均衡所用的算法
     * @return
     */
    @Bean
    public IRule myRule(){
        //return new RandomRule();//达到的目的，用我们重新选择的随机算法替代默认的轮询
        return new RetryRule();
    }

}

