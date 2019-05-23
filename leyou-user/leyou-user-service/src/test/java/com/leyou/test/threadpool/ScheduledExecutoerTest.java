package com.leyou.test.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class Temp implements Runnable {

    @Override
    public void run() {
        System.out.println("run ...");
    }
}

public class ScheduledExecutoerTest {

    public static void main(String[] args) {

        Temp temp = new Temp();

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

        //ScheduledFuture<?> scheduledFuture = pool.scheduleWithFixedDelay(temp, 1, 1, TimeUnit.SECONDS);

        ScheduledFuture<?> scheduledFuture1 = pool.scheduleAtFixedRate(temp, 1, 1, TimeUnit.SECONDS);
    }
}
