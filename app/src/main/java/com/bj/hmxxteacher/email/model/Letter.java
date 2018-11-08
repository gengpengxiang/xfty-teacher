package com.bj.hmxxteacher.email.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/10/26 0026.
 *
 *private boolean isSelected;
 private boolean isLongPress;
 */

public class Letter {


    /**
     * ret : 3
     * msg : 未读查询成功
     * data : {"list_data":[{"jiazhang":"金爸爸","date":"2018-10-29","class":"一年级1班","title":"6K++6aKY5Lit5pyf5oql5ZGK5Lya"}],"xinjian_num":2,"star_num":1}
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
         * list_data : [{"jiazhang":"金爸爸","date":"2018-10-29","class":"一年级1班","title":"6K++6aKY5Lit5pyf5oql5ZGK5Lya"}]
         * xinjian_num : 2
         * star_num : 1
         */

        private int xinjian_num;
        private int star_num;
        private List<ListDataBean> list_data;

        public int getXinjian_num() {
            return xinjian_num;
        }

        public void setXinjian_num(int xinjian_num) {
            this.xinjian_num = xinjian_num;
        }

        public int getStar_num() {
            return star_num;
        }

        public void setStar_num(int star_num) {
            this.star_num = star_num;
        }

        public List<ListDataBean> getList_data() {
            return list_data;
        }

        public void setList_data(List<ListDataBean> list_data) {
            this.list_data = list_data;
        }

        public static class ListDataBean {
            /**
             * jiazhang : 金爸爸
             * date : 2018-10-29
             * class : 一年级1班
             * title : 6K++6aKY5Lit5pyf5oql5ZGK5Lya
             * "star_status": "0"
             * "yidu_status": "0",
             *" xinjianid": "6"
             */

            private String jiazhang;
            private String date;

            private String class_name;
            private String title;
            private String star_status;
            private String yidu_status;
            private String xinjianid;

            private boolean isSelected;
            private boolean isLongPress;

            public String getYidu_status() {
                return yidu_status;
            }

            public void setYidu_status(String yidu_status) {
                this.yidu_status = yidu_status;
            }

            public String getXinjianid() {
                return xinjianid;
            }

            public void setXinjianid(String xinjianid) {
                this.xinjianid = xinjianid;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public boolean isLongPress() {
                return isLongPress;
            }

            public void setLongPress(boolean longPress) {
                isLongPress = longPress;
            }

            public String getJiazhang() {
                return jiazhang;
            }

            public void setJiazhang(String jiazhang) {
                this.jiazhang = jiazhang;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getClass_name() {
                return class_name;
            }

            public void setClass_name(String class_name) {
                this.class_name = class_name;
            }

            public String getStar_status() {
                return star_status;
            }

            public void setStar_status(String star_status) {
                this.star_status = star_status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
