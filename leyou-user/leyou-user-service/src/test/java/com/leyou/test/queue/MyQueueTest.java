package com.leyou.test.queue;

public class MyQueueTest {

    public static void main(String[] args) throws InterruptedException {

        MyQueue myQueue = new MyQueue(5);
//        myQueue.put("a");
//        myQueue.put("b");
//        myQueue.put("c");
//        myQueue.put("d");
//        myQueue.put("e");

        System.out.println("容量：" + myQueue.size());

        new Thread(() -> {
            myQueue.get();

            myQueue.get();
        }).start();

        new Thread(() -> {
            myQueue.put("x");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myQueue.put("y");
        }).start();

    }
}
