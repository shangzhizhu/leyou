package com.leyou.test.masterworker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Worker implements Runnable{
    private ConcurrentLinkedDeque<Task> masterQueue;
    private ConcurrentHashMap<String, Integer> resultMap;
    public void setConcurrentLinkedDeque(ConcurrentLinkedDeque<Task> masterQueue) {
        this.masterQueue = masterQueue;
    }
    public void setConcurrentHashMap(ConcurrentHashMap<String, Integer> resultMap) {
        this.resultMap = resultMap;
    }
    @Override
    public void run() {
        while (true){
            Task task = this.masterQueue.poll();
            if (task == null) break;
            // 处理业务
            int ret = handler(task);
            resultMap.put(Integer.toString(task.getId()), ret);
        }
    }

    private int handler(Task task) {
        int ret = 0;
        // 模拟处理业务的用时
        try {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " 处理task: " + task.getId());
            ret = task.getPrice();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
