package com.leyou.test.concurrentpackge;

import java.util.concurrent.CountDownLatch;

public class TestCountDownLatch {

    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(() -> {
            try {
                System.out.println("线程1等待其他线程通知...");
                countDownLatch.await();
                System.out.println("线程1执行...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            System.out.println("线程2通知线程1...");
            countDownLatch.countDown();
            System.out.println("线程2继续执行");
        }).start();

        new Thread(() -> {
            System.out.println("线程3通知线程1...");
            countDownLatch.countDown();
            System.out.println("线程3继续执行");
        }).start();
    }
}
