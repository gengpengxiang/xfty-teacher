package com.bj.hmxxteacher.grade.model;

import java.util.List;

/**
 * Created by Administrator on 2018/11/1 0001.
 */

public class Position {


    /**
     * ret : 1
     * msg : 查询成功
     * data : [{"juese_code":"01","juese_name":"校长"},{"juese_code":"03","juese_name":"低年级主任"}]
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
         * juese_code : 01
         * juese_name : 校长
         */

        private String juese_code;
        private String juese_name;

        public String getJuese_code() {
            return juese_code;
        }

        public void setJuese_code(String juese_code) {
            this.juese_code = juese_code;
        }

        public String getJuese_name() {
            return juese_name;
        }

        public void setJuese_name(String juese_name) {
            this.juese_name = juese_name;
        }
    }
}
