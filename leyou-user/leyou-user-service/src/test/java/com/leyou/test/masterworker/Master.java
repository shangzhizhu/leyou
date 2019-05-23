package com.leyou.test.masterworker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Master {
    // 任务的容器
    private ConcurrentLinkedDeque<Task> masterQueue = new ConcurrentLinkedDeque<>();
    // worker容器
    private HashMap<String, Thread> workers = new HashMap<>();
    // worker返回结果集
    private ConcurrentHashMap<String, Integer> resultMap = new ConcurrentHashMap<>();
    // 构造方法
    public Master(Worker worker, int count){
        worker.setConcurrentLinkedDeque(this.masterQueue);
        worker.setConcurrentHashMap(this.resultMap);
        for (int i = 0; i < count; i++) {
            workers.put(Integer.toString(i), new Thread(worker, "线程" + i));
        }
    }
    // 提交任务的方法
    public void submit(Task task){
        this.masterQueue.add(task);
    }
    // 执行任务
    public void execute(){
        workers.forEach((s, t) -> {
            t.start();
        });
    }
    // 判断线程是否执行完毕
    public boolean isComplete(){
        for(Map.Entry<String, Thread> entry: workers.entrySet()){
            if (entry.getValue().getState() != Thread.State.TERMINATED){
                return false;
            }
        }
        return true;
    }
    // 结果集汇总
    public int getResult(){
        int result = 0;
        for(Map.Entry<String, Integer> entry: resultMap.entrySet()){
            result += entry.getValue();
        }
        return result;
    }
}
