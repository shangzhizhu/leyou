package com.leyou.test.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class Task implements Runnable {

    private int id;

    private String name;

    public Task(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.id);
    }
}
public class TestMy {

    public static void main(String[] args) {

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                                1,
                                2,
                                60,
                                TimeUnit.SECONDS,
                                new LinkedBlockingQueue<>());


        Task t1 = new Task(1, "renwu 1");
        Task t2 = new Task(2, "renwu 2");
        Task t3 = new Task(3, "renwu 3");
        Task t4 = new Task(4, "renwu 4");
        Task t5 = new Task(5, "renwu 5");
        Task t6 = new Task(6, "renwu 6");

        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        pool.execute(t4);
        pool.execute(t5);
        pool.execute(t6);

        pool.shutdown();
    }
}
