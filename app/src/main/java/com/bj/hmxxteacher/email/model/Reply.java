package com.bj.hmxxteacher.email.model;

import java.util.List;

/**
 * Created by Administrator on 2018/10/29 0029.
 */

public class Reply {


    /**
     * ret : 1
     * msg : 查询成功
     * data : {"title":"6K++6aKY5Lit5pyf5oql5ZGK5Lya","date":"2018-10-29 09:45:55","content":"5L2g5biM5pyb5a2p5a2Q5aSN5Lmg5pe255qE5pWI546H","huifu_list":[{"name":"逗号博士","time":"2小时前","huifu_content":"5L2g5Lus5aW9"}]}
     */

    private String ret;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : 6K++6aKY5Lit5pyf5oql5ZGK5Lya
         * date : 2018-10-29 09:45:55
         * content : 5L2g5biM5pyb5a2p5a2Q5aSN5Lmg5pe255qE5pWI546H
         * huifu_list : [{"name":"逗号博士","time":"2小时前","huifu_content":"5L2g5Lus5aW9"}]
         */

        private String title;
        private String date;
        private String content;
        private List<HuifuListBean> huifu_list;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<HuifuListBean> getHuifu_list() {
            return huifu_list;
        }

        public void setHuifu_list(List<HuifuListBean> huifu_list) {
            this.huifu_list = huifu_list;
        }

        public static class HuifuListBean {
            /**
             * name : 逗号博士
             * time : 2小时前
             * huifu_content : 5L2g5Lus5aW9
             */

            private String name;
            private String time;
            private String huifu_content;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getHuifu_content() {
                return huifu_content;
            }

            public void setHuifu_content(String huifu_content) {
                this.huifu_content = huifu_content;
            }
        }
    }
}
