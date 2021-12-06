package com.atguigu.www.case01;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 服务器动态上下线鉴定案例
 * 服务器端向 Zookeeper 注册代码
 * 服务端创建是临时节点，创建节点的服务器断开连接时，节点自动删除
 * 启动前需要在main函数设置args的参数Program arguments
 */
public class DistributeServer {
    private static String connectString="my01:2181,my03:2181,my04:2181"; //zookeeper集群
    private static int  sessionTimeout=3000;//连接时间
    private ZooKeeper zk =null;
    private  String parentNode="/servers";//集群根节点
    
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        DistributeServer distributeServer = new DistributeServer();
        //1.获取到zookeeper的连接
        distributeServer.getConnect();
        //2.注册服务器
        distributeServer.registServer(args[0]);
        //3.业务功能
        distributeServer.business(args[0]);
    }

    //连接Zookeeper
    private void getConnect() throws IOException {
        zk=new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }
    //注册服务器
    private void registServer(String hostname) throws InterruptedException, KeeperException {
        String create = zk.create("/servers/", hostname.getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostname+" is successfully registered！" +create);
    }
    //业务逻辑，此处暂时为睡眠
    private void business(String hostname) throws InterruptedException {
        System.out.println(hostname +" is working ...");
        Thread.sleep(Long.MAX_VALUE);
    }
}
