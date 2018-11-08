package com.bj.hmxxteacher.reason.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/10/25 0025.
 */

public class Reason implements Serializable {

    /**
     * ret : 1
     * msg :
     * data : [{"id":"03","type":"1","title":"待人礼貌"},{"id":"1898888888818102508005764","type":"1","title":"热爱生活"},{"id":"1898888888818102508012184","type":"1","title":"热爱学习"}]
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

    public static class DataBean implements Serializable{
        /**
         * "code": "06",
         * "liyou": "课堂积极",
         * "img": "dianzan/2.png"
         */


        private String code;
        private String liyou;
        private String img;
        public boolean isLast;

        public DataBean() {
            super();
        }

        public DataBean(String code, String liyou, String img) {
            super();
            this.code = code;
            this.liyou = liyou;
            this.img = img;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getLiyou() {
            return liyou;
        }

        public void setLiyou(String liyou) {
            this.liyou = liyou;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public boolean isLast() {
            return isLast;
        }

        public void setLast(boolean last) {
            isLast = last;
        }
    }
}
