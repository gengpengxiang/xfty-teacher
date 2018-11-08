package com.bj.hmxxteacher.homeclass.model;

import java.util.List;

/**
 * Created by Administrator on 2018/11/1 0001.
 */

public class Dynamic {

    /**
     * ret : 1
     * msg :
     * data : [{"dongtai_id":"37310","dongtai_pic":"z1.png","dongtai_type":"z1","dongtai_title":"热心助人","dongtai_time":"2018-10-29","dongtai_ganxiestatus":"1","teacher_pic":"09ee0bc0e6b219dd54081edd7d5be37c.jpeg","student_pic":"77ad3e9babd6ffc6d810bde717d7e127.jpeg","student_name":"崔艳洋","student_pid":"010116010","huizhang_shuoming":"","teacher_phone":"18988888888","jiazhang_phone":"13716219050"}]
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
         * dongtai_id : 37310
         * dongtai_pic : z1.png
         * dongtai_type : z1
         * dongtai_title : 热心助人
         * dongtai_time : 2018-10-29
         * dongtai_ganxiestatus : 1
         * teacher_pic : 09ee0bc0e6b219dd54081edd7d5be37c.jpeg
         * student_pic : 77ad3e9babd6ffc6d810bde717d7e127.jpeg
         * student_name : 崔艳洋
         * student_pid : 010116010
         * huizhang_shuoming :
         * teacher_phone : 18988888888
         * jiazhang_phone : 13716219050
         */

        private String dongtai_id;
        private String dongtai_pic;
        private String dongtai_type;
        private String dongtai_title;
        private String dongtai_time;
        private String dongtai_ganxiestatus;
        private String teacher_pic;
        private String student_pic;
        private String student_name;
        private String student_pid;
        private String huizhang_shuoming;
        private String teacher_phone;
        private String jiazhang_phone;

        public String getDongtai_id() {
            return dongtai_id;
        }

        public void setDongtai_id(String dongtai_id) {
            this.dongtai_id = dongtai_id;
        }

        public String getDongtai_pic() {
            return dongtai_pic;
        }

        public void setDongtai_pic(String dongtai_pic) {
            this.dongtai_pic = dongtai_pic;
        }

        public String getDongtai_type() {
            return dongtai_type;
        }

        public void setDongtai_type(String dongtai_type) {
            this.dongtai_type = dongtai_type;
        }

        public String getDongtai_title() {
            return dongtai_title;
        }

        public void setDongtai_title(String dongtai_title) {
            this.dongtai_title = dongtai_title;
        }

        public String getDongtai_time() {
            return dongtai_time;
        }

        public void setDongtai_time(String dongtai_time) {
            this.dongtai_time = dongtai_time;
        }

        public String getDongtai_ganxiestatus() {
            return dongtai_ganxiestatus;
        }

        public void setDongtai_ganxiestatus(String dongtai_ganxiestatus) {
            this.dongtai_ganxiestatus = dongtai_ganxiestatus;
        }

        public String getTeacher_pic() {
            return teacher_pic;
        }

        public void setTeacher_pic(String teacher_pic) {
            this.teacher_pic = teacher_pic;
        }

        public String getStudent_pic() {
            return student_pic;
        }

        public void setStudent_pic(String student_pic) {
            this.student_pic = student_pic;
        }

        public String getStudent_name() {
            return student_name;
        }

        public void setStudent_name(String student_name) {
            this.student_name = student_name;
        }

        public String getStudent_pid() {
            return student_pid;
        }

        public void setStudent_pid(String student_pid) {
            this.student_pid = student_pid;
        }

        public String getHuizhang_shuoming() {
            return huizhang_shuoming;
        }

        public void setHuizhang_shuoming(String huizhang_shuoming) {
            this.huizhang_shuoming = huizhang_shuoming;
        }

        public String getTeacher_phone() {
            return teacher_phone;
        }

        public void setTeacher_phone(String teacher_phone) {
            this.teacher_phone = teacher_phone;
        }

        public String getJiazhang_phone() {
            return jiazhang_phone;
        }

        public void setJiazhang_phone(String jiazhang_phone) {
            this.jiazhang_phone = jiazhang_phone;
        }
    }
}
