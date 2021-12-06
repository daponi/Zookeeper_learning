package com.atguigu.www.case02;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;

/**
 *实现分布式锁案例--测试
 */
@SuppressWarnings("all")
public class DistributedLockTest {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        // 创建分布式锁 1, 匿名内部类想用方法中定义的局部变量必须加final,jdk1.8之后不用加了但还是final的,不final的话线程里面无法调用
         DistributedLock lock01 = new DistributedLock();
        // 创建分布式锁 2, 匿名内部类想用方法中定义的局部变量必须加final,jdk1.8之后不用加了但还是final的,不final的话线程里面无法调用
         DistributedLock lock02 = new DistributedLock();

        /**
         * 客户端01，线程是模拟多个客户端同时访问集群数据
         * 线程执行顺序要看哪个线程先抢到cpu，和编码顺序无关，本来cpu调度随机的，是异步
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
            // 获取锁对象
                try {
                    lock01.zkLock();
                    System.out.println("\n"+"线程 1 获取锁");
                    //延时是为让你看到5秒后，线程2获取到锁
                    Thread.sleep(5 * 1000);
                    lock01.zkUnlock();
                    System.out.println("线程 1 释放锁");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        /**
         * 客户端02，线程是模拟多个客户端同时访问集群数据
         * 线程执行顺序要看哪个线程先抢到cpu，和编码顺序无关，本来cpu调度随机的，是异步
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
            // 获取锁对象
                try {
                    lock02.zkLock();
                    System.out.println("线程 2 获取锁");
                    Thread.sleep(5 * 1000);
                    lock02.zkUnlock();
                    System.out.println("线程 2 释放锁");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
