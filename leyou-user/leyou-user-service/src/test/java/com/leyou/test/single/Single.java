package com.leyou.test.single;

public class Single {

    private Single(){}

    private static class SingleHandler {
        private static final Single SINGLE = new Single();
    }

    public static Single getSingle(){
        return SingleHandler.SINGLE;
    }


    public static void main(String[] args) {

        new Thread(()->{
            Single single = Single.getSingle();
            System.out.println(single);
        }).start();

        new Thread(()->{
            Single single = Single.getSingle();
            System.out.println(single);
        }).start();

        new Thread(()->{
            Single single = Single.getSingle();
            System.out.println(single);
        }).start();
    }
}
