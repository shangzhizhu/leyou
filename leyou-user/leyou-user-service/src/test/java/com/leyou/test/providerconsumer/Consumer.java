package com.leyou.test.providerconsumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{

    private BlockingQueue<Data> queue;

    private static Random r = new Random();

    public Consumer(BlockingQueue<Data> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true){
            try {
                Data take = this.queue.take();
                Thread.sleep(r.nextInt(1000));
                System.out.println(Thread.currentThread().getName() + "从队列中获取并消费了id为:" + take.getId() + "的数据");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
