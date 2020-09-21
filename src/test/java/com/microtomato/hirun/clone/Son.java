package com.microtomato.hirun.clone;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steven
 * @date 2020-09-17
 */
public class Son extends Father implements Cloneable {

    protected Long id;
    protected String name;
    protected final List<String> hobbys = new ArrayList<>();

    public Son() {
        this.id = 2L;
        this.name = "儿子";
        this.hobbys.add("跑步");
        this.hobbys.add("站桩");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id: " + this.id);
        sb.append(", name: " + this.name);
        sb.append(", hobbys: " + this.hobbys);
        sb.append(", fatherId: " + super.id);
        sb.append(", fatherName: " + super.name);
        sb.append(", fatherHobbys: " + super.hobbys);
        sb.append(", phoneNumber: " + super.phoneNumber);
        return sb.toString();
    }

}
