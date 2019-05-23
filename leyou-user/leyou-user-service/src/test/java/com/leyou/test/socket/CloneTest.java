package com.leyou.test.socket;

public class CloneTest {

    public static void main(String[] args) throws CloneNotSupportedException {

        Person person = new Person();
        System.out.println(person);
        Person p = (Person)person.clone();
        System.out.println(p);

    }
}
