package com.leyou.test.queue;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义一个先进先出的阻塞队列
 */
public class MyQueue {

    // 定义LinkedList作为队列容器
    private final LinkedList<Object> list = new LinkedList<>();

    // 定义容器最小size
    private int minSize = 0;

    // 定义容器最大size
    private int maxSize;

    // 定义计数器 记录容器大小的变化
    private final AtomicInteger count = new AtomicInteger(0);

    // 定义对象锁
    private final Object lock = new Object();

    public MyQueue(int size){
        if (size < 0)
            throw new RuntimeException("容器长度不能小于0");
        this.maxSize = size;
    }

    /**
     * 添加元素的方法
     * @param obj
     */
    public void put(Object obj){
        synchronized (lock){
            if (count.get() == this.maxSize){
                // 如果添加元素时容器已经满了 阻塞put
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.addLast(obj);
            count.incrementAndGet();
            System.out.println("添加了元素：" + obj);
            // 极端情况下get方法在阻塞 添加第一个元素时要唤醒处于get阻塞的线程
            lock.notify();
        }
    }

    /**
     * 获取元素的方法
     * @return
     */
    public Object get(){
        Object ret;
        synchronized (lock){
            if (count.get() == this.minSize){
                // 如果获取元素时容器是空的 阻塞get
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ret = list.removeFirst();
            count.decrementAndGet();
            System.out.println("获取了元素：" + ret);
            // 极端情况下put方法在阻塞 获取元素时要唤醒处于put阻塞的线程
            lock.notify();
        }
        return ret;
    }

    /**
     * 获取当前容器的size
     * @return
     */
    public int size(){
        return count.get();
    }

}
