package com.microtomato.hirun.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author Steven
 * @date 2019-12-28
 */
public class LinkedHashSetTest {
    public static void main(String[] args) {
        LinkedHashSet<Long> set = new LinkedHashSet<>();
        for (long i = 1000L; i > 0L; i--) {
            set.add(i);
        }

        List<Long> list = new ArrayList<>();
        list.addAll(set);

        for (Long aLong : list) {
            System.out.println(aLong);
        }
    }
}
