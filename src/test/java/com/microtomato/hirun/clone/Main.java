package com.microtomato.hirun.clone;

import java.util.ArrayList;

/**
 * @author Steven
 * @date 2020-09-17
 */
public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {
        Son son1 = new Son();
        Son son2 = (Son) son1.clone();
        System.out.println(son1);
        System.out.println("==================");
        son2.id = 3L;
        son2.name = "大儿子";
        son2.phoneNumber = "123321";

        System.out.println(son1);
        System.out.println(son2);
    }

}
