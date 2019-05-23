package com.leyou.test.jvmtest;

public class JvmTest1 {


    public static void main(String[] args) {

        Runtime runtime = Runtime.getRuntime();

        System.out.println("max memory: " + runtime.maxMemory()/(1024 * 1024));
        System.out.println("free memory: " + runtime.freeMemory()/(1024 * 1024));
        System.out.println("total memory: " + runtime.totalMemory()/(1024 * 1024));

        byte[] bytes = new byte[1024 * 1024];
        System.out.println("max memory: " + runtime.maxMemory()/(1024 * 1024));
        System.out.println("free memory: " + runtime.freeMemory()/(1024 * 1024));
        System.out.println("total memory: " + runtime.totalMemory()/(1024 * 1024));

        byte[] bytes2 = new byte[4 * 1024 * 1024];
        System.out.println("max memory: " + runtime.maxMemory()/(1024 * 1024));
        System.out.println("free memory: " + runtime.freeMemory()/(1024 * 1024));
        System.out.println("total memory: " + runtime.totalMemory()/(1024 * 1024));
    }
}
