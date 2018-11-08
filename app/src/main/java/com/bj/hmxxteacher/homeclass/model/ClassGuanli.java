package com.bj.hmxxteacher.homeclass.model;

import com.bj.hmxxteacher.grade.model.GradeGuanlian;

import java.util.List;

/**
 * Created by Administrator on 2018/11/1 0001.
 */

public class ClassGuanli {
    private String ret;
    private String msg;
    private List<ClassGuanli.DataBean> data;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ClassGuanli.DataBean> getData() {
        return data;
    }

    public void setData(List<ClassGuanli.DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * teacherphone : 18988888888
         * classcode : 000401
         * class_name : 班级不存在
         */

        private String teacherphone;
        private String classcode;
        private String class_name;

        public String getTeacherphone() {
            return teacherphone;
        }

        public void setTeacherphone(String teacherphone) {
            this.teacherphone = teacherphone;
        }

        public String getClasscode() {
            return classcode;
        }

        public void setClasscode(String classcode) {
            this.classcode = classcode;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }
    }
}
