package com.leyou.test.jvmtest;

public class TestString {

    public static void main(String[] args) {

        String test = test();

        String a = "a";

        String b = "b";

        String ab = a + "b";

        String ab1 = "a" + "b";

        System.out.println(test == ab);

        String ab2 = a + b;

        String ab3 = "a" + "b" + "";

        System.out.println(ab2 == "ab");

        System.out.println(ab2 == ab);

        System.out.println(ab3 == "ab");

        System.out.println(ab3 == ab1);

        Thread thread = new Thread();
        thread.start();

    }

    public static String test(){
        String a = "a";
        String ab = a + "b";
        return ab;
    }
}
