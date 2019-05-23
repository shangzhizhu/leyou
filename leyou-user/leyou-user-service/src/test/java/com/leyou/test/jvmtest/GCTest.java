package com.leyou.test.jvmtest;

public class GCTest {

    public static void main(String[] args) {
        String str = "";
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            str += i + str;
            str.intern();
        }
    }
}
