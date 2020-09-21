package com.microtomato.hirun.clone;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steven
 * @date 2020-09-17
 */
public class Father {
    protected Long id;
    protected String phoneNumber;
    protected String name;
    protected final List<String> hobbys = new ArrayList<>();

    public Father() {
        this.id = 1L;
        this.phoneNumber = "123";
        this.name = "爸爸";
        this.hobbys.add("散步");
        this.hobbys.add("喝茶");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id: " + this.id);
        sb.append(", name: " + this.name);
        sb.append(", hobbys: " + this.hobbys);
        sb.append(", phoneNumber: " + this.phoneNumber);
        return sb.toString();
    }

}
