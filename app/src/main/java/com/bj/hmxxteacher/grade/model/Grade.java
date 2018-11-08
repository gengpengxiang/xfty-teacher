package com.bj.hmxxteacher.grade.model;

import java.util.List;

/**
 * Created by Administrator on 2018/10/31 0031.
 */

public class Grade {


    /**
     * ret : 1
     * msg : 查询成功
     * data : [{"nianji_code":"1","nianji_name":"一年级"},{"nianji_code":"2","nianji_name":"二年级"},{"nianji_code":"3","nianji_name":"三年级"},{"nianji_code":"4","nianji_name":"四年级"},{"nianji_code":"5","nianji_name":"五年级"},{"nianji_code":"6","nianji_name":"六年级"}]
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
         * nianji_code : 1
         * nianji_name : 一年级
         */

        private String nianji_code;
        private String nianji_name;

        public String getNianji_code() {
            return nianji_code;
        }

        public void setNianji_code(String nianji_code) {
            this.nianji_code = nianji_code;
        }

        public String getNianji_name() {
            return nianji_name;
        }

        public void setNianji_name(String nianji_name) {
            this.nianji_name = nianji_name;
        }
    }
}
