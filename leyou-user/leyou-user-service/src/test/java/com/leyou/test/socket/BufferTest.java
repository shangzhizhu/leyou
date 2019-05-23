package com.leyou.test.socket;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class BufferTest {

    public static void main(String[] args) {

        IntBuffer buffer = IntBuffer.allocate(10);
        buffer.put(2);
        buffer.put(3);
        buffer.put(5);

        buffer.flip();
        System.out.println(buffer);
        System.out.println(buffer.capacity());
        System.out.println(buffer.limit());

        System.out.println("获取下标为1的元素:" + buffer.get(1));
        System.out.println(buffer);

        buffer.put(1, 4);
        System.out.println(buffer);

        for (int i = 0; i < buffer.limit(); i++) {
            System.out.println(buffer.get());
        }

        Object o = new Object();

        ArrayList<Object> objects = new ArrayList<>();
        LinkedList<Object> objects1 = new LinkedList<>();
        HashMap<Object, Object> map = new HashMap<>();
        map.put(null, 1);
        map.put(null, 2);
        System.out.println(map);

        int a = 0;
        int b = a++;
        System.out.println("b=" + b);
    }
}
