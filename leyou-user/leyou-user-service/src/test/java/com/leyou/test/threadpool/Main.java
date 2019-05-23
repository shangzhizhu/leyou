package com.leyou.test.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newScheduledThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "execute..");
            });
        }

        Thread.sleep(3000);

        executorService.execute(() -> {
            System.out.println(Thread.currentThread().getName() + "... execute..");
        });
    }
}
