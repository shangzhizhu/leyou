package com.leyou.test.masterworker;

import java.util.Random;

public class TestModel {

    public static void main(String[] args) {

        Master master = new Master(new Worker(), 10);

        Random random = new Random();
        for (int i = 1; i <= 100; i++) {
            Task task = new Task();
            task.setId(i);
            task.setName("任务编号" + i);
            task.setPrice(random.nextInt(1000));
            master.submit(task);
        }

        master.execute();
        long start = System.currentTimeMillis();

        while (true){
            if (master.isComplete()){
                long end = System.currentTimeMillis();
                int result = master.getResult();
                System.out.println("执行最终结果：" + result + " 共耗时: " + (end - start));
                break;
            }
        }


    }
}
