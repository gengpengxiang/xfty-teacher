package com.bj.hmxxteacher.grade.model;

import java.util.List;

/**
 * Created by Administrator on 2018/10/31 0031.
 */

public class GradeGuanlian {


    /**
     * ret : 1
     * msg :
     * data : [{"teacherphone":"18988888888","classcode":"000401","class_name":"班级不存在"},{"teacherphone":"18988888888","classcode":"000203","class_name":"一年级1班"}]
     */

    private String ret;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
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
