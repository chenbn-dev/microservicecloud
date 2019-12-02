package com.atguigu.myrule;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.List;
/*
八 ILoadBalancer接口源码分析

该接口中定义了一个客户端负载均衡器需要的一系列抽象操作

1 addServers：向负载均衡器中维护的实例列表中增加服务实例。

2 chooseServer：通过某种策略，从负载均衡器中挑选出一个具体的服务实例。

3 markServerDown：用来通知和标识负载均衡器中某个具体实例已经停止服务，不然负载均衡器在下一次获取服务实例清单都会认为该服务实例时正常的。

4 getReachableServers：获取当前服务的实例列表。

5 getAllServers：获取所有已经的服务实例列表，包括正常服务和停止服务的实例。

在该接口定义中涉及Server对象定义时一个传统的服务端节点，在该类中存储了服务端节点的一些元数据信息，包括host、port一些部署信息等。
 */
public class RandomRule_ZY extends AbstractLoadBalancerRule {

    // total=0 // 当total==5以后，我们才能往下走
    // index=0 // 当前对外提供服务的服务器地址
    // total需要重新置为零，但是已经达到过一个5次，我们的 index=1
    // 分析：我们5次，但是微服务只有8001 8002 8003 三台

    private int total = 0;// 总共被调用的次数，目前要求每台机子被调用5次
    private int currentIndex = 0;   // 当前提供服务的机器号


    public Server choose(ILoadBalancer lb , Object key) {
        if(lb == null){
            return null;
        }
        Server server = null;
        while (server == null){
            if(Thread.interrupted()){ // Thread.interrupted()：测试当前执行的线程是否已经中断，会清除线程的中断状态。
                return  null;
            }
            List<Server> upList = lb.getReachableServers();// getReachableServers：获取当前服务的实例列表。
            List<Server> allList = lb.getAllServers();//getAllServers：获取所有已经的服务实例列表，包括正常服务和停止服务的实例。

            int serverCount = allList.size();
            if(serverCount == 0){
                return null;
            }

            if(total < 5){
                server = upList.get(currentIndex);
                total ++;
            }else {
                total = 0;
                currentIndex ++;
                if(currentIndex >= upList.size()){
                    currentIndex = 0;
                }
            }

            if(server == null){
                Thread.yield();
                continue;
            }
            if(server.isAlive()){
                return (server);
            }
            server = null;
            Thread.yield();
        }

        return server;
    }
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }


    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }
}
