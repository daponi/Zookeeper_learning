package com.atguigu.www;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 创建 ZooKeeper 客户端 的Demo
 */
public class ZkClient {
    //注意逗号后面不能有空格
    private static String ConnectString ="my01:2181,my03:2181,my04:2181";
    private static int  sessionTimeout=3000;
    private ZooKeeper zkClient;

//    @Test
    @Before
    public void init() throws IOException {
        zkClient = new ZooKeeper(ConnectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                // 收到事件通知后的回调函数（用户的业务逻辑）

                System.out.println("\n"+"========"+watchedEvent.getType() + "--"+ watchedEvent.getPath()+"========");
                // 再次启动监听
                try {
                    List<String> children = zkClient.getChildren("/",
                            true);
                    for (String child : children) {
                        System.out.println("watcher()输出:"+ child);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("======== 完毕 ========");
            }
        });
    }

    /**
     * 创建节点
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void create() throws InterruptedException, KeeperException {
        // 参数 1：要创建的节点的路径； 参数 2：节点数据 ； 参数 3：节点权限 ； 参数 4：节点的类型
        zkClient.create("/atguigu", "Hello,World！你好".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 获取对应路径下的节点
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void getChildren() throws InterruptedException, KeeperException {
        /**
         * path : 获取该路径下的节点
         * wath : 启动连接时的监听器
         */
        List<String> children = zkClient.getChildren("/", true);
        for (String child : children) {
            System.out.println("getChildren()---输出: " +child);
        }
        // 延时阻塞,方便监听器线程一直开启
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 判断 znode 是否存在
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void exists() throws InterruptedException, KeeperException {
        Stat exists = zkClient.exists("/atguigu", false);
        System.out.println(exists == null ? "not exist" : "exist");
        System.out.println(exists);
    }

}
