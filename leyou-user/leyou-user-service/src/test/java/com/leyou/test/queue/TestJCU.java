package com.leyou.test.queue;

import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;

public class TestJCU {

    @Test
    public void test1(){

        LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<>(2);
        queue.offer("a");
        queue.offer("b");
        queue.add("c");

        queue.forEach(q -> {
            System.out.println(q);
        });
    }
}
