package com.atguigu.www.case01;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务器动态上下线鉴定案例
 * 客户端端向 Zookeeper 注册代码
 */
public class DistributeClient {
    private static String connectString="my01:2181,my03:2181,my04:2181"; //zookeeper集群
    private static int  sessionTimeout=3000;//连接时间
    private ZooKeeper zk =null;
    private  String parentNode="/servers";//集群根节点
    
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        DistributeClient distributeServer = new DistributeClient();
        //1.获取到zookeeper的连接
        distributeServer.getConnect();
        //2.监听/servers下面子节点的增加和删除
        distributeServer.getServerList();
        //3.业务功能
        distributeServer.business();
    }



    //连接Zookeeper
    private void getConnect() throws IOException {
        zk=new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                // 再次启动监听
                try {
                    getServerList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    // 获取服务器列表信息
    private void getServerList() throws InterruptedException, KeeperException {
        // 1 获取服务器子节点信息，并且对父节点进行监听
        List<String> children = zk.getChildren(parentNode , true);
        // 2 存储服务器信息列表
        ArrayList<String> servers = new ArrayList<>();
        // 3 遍历所有节点，获取节点中的主机名称信息
        for (String child : children) {
            //1.根节点，2.是否使用zk的监听器，3.显示详细信息
            byte[] data = zk.getData(parentNode + "/"+child, false, null);
            servers.add(new String(data));
        }
        // 4 打印服务器列表信息
        System.out.println(servers);
    }
    //业务逻辑，此处暂时为睡眠
    private void business() throws InterruptedException {
        System.out.println("Child is working ...");
        Thread.sleep(Long.MAX_VALUE);
    }
}
