package com.leyou.test.jvmtest;

import java.util.LinkedList;

public class JvmTest {

    public static void main(String[] args) {

        String str = new String("haha");
        str.intern();
        System.out.println(str.getClass().getClassLoader());

        System.out.println("--------------------------------");

        Member member = new Member();
        System.out.println(member.getClass().getClassLoader());
        System.out.println(member.getClass().getClassLoader().getParent());
        System.out.println(member.getClass().getClassLoader().getParent().getParent());

        LinkedList<Object> linkedList = new LinkedList<>();
        linkedList.add("a");
    }



}
