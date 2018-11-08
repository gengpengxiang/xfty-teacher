package com.bj.hmxxteacher.grade.model;

import java.util.List;

/**
 * Created by Administrator on 2018/11/1 0001.
 */

public class ClassInfo {

    /**
     * ret : 2
     * msg : 查询成功
     * data : [{"classcode":"000203","name":"一年级1班","dianzan":"0","gaijin":"3","zongfen":"-3"},{"classcode":null,"name":null,"dianzan":null,"gaijin":null,"zongfen":null}]
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
         * classcode : 000203
         * name : 一年级1班
         * dianzan : 0
         * gaijin : 3
         * zongfen : -3
         */

        private String classcode;
        private String name;
        private String dianzan;
        private String gaijin;
        private String zongfen;

        public String getClasscode() {
            return classcode;
        }

        public void setClasscode(String classcode) {
            this.classcode = classcode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDianzan() {
            return dianzan;
        }

        public void setDianzan(String dianzan) {
            this.dianzan = dianzan;
        }

        public String getGaijin() {
            return gaijin;
        }

        public void setGaijin(String gaijin) {
            this.gaijin = gaijin;
        }

        public String getZongfen() {
            return zongfen;
        }

        public void setZongfen(String zongfen) {
            this.zongfen = zongfen;
        }
    }
}
