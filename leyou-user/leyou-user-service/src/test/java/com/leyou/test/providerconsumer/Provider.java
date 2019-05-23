package com.leyou.test.providerconsumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Provider implements Runnable{

    private BlockingQueue<Data> queue;

    private volatile boolean isRunning = true;

    private static AtomicInteger count = new AtomicInteger();

    private static Random r = new Random();

    public Provider(BlockingQueue<Data> queue){
       this.queue = queue;
    }

    @Override
    public void run() {
        while (isRunning){
            try {
                Thread.sleep(r.nextInt(1000));
                int id = count.incrementAndGet();
                Data data = new Data(id, "数据" + id);
                System.out.println(Thread.currentThread().getName() + "生产了id为:" + id + "的数据，放到队列中");
                if (!this.queue.offer(data, 2, TimeUnit.SECONDS)){
                    System.out.println("生产者生产数据到队列失败");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        this.isRunning = false;
    }
}
