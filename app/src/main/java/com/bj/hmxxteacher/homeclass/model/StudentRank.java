package com.bj.hmxxteacher.homeclass.model;

/**
 * Created by Administrator on 2018/11/2 0002.
 */

public class StudentRank {

    private String name;
    private String number;

    public StudentRank(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
