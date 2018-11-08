package com.bj.hmxxteacher.bean;

/**
 * Created by Administrator on 2018/11/3 0003.
 */

public class TeacherInfos {

    /**
     * ret : 1
     * msg :
     * data : {"teacherphone":"18911111111","name":"逗号老师","phoneyzm":"123456","updatetime":"2018-12-09 10:24:04","teacherimg":"","teacherxueke":"01","schoolcode":"0002","schoolname":"逗号学校","schoolimg":"school/schoolimg0003.png","nicheng":"","type":"0"}
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
         * teacherphone : 18911111111
         * name : 逗号老师
         * phoneyzm : 123456
         * updatetime : 2018-12-09 10:24:04
         * teacherimg :
         * teacherxueke : 01
         * schoolcode : 0002
         * schoolname : 逗号学校
         * schoolimg : school/schoolimg0003.png
         * nicheng :
         * type : 0
         * moren_classcode:"
         */

        private String teacherphone;
        private String name;
        private String phoneyzm;
        private String updatetime;
        private String teacherimg;
        private String teacherxueke;
        private String schoolcode;
        private String schoolname;
        private String schoolimg;
        private String nicheng;
        private String type;
        private String moren_classcode;

        public String getMoren_classcode() {
            return moren_classcode;
        }

        public void setMoren_classcode(String moren_classcode) {
            this.moren_classcode = moren_classcode;
        }

        public String getTeacherphone() {
            return teacherphone;
        }

        public void setTeacherphone(String teacherphone) {
            this.teacherphone = teacherphone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneyzm() {
            return phoneyzm;
        }

        public void setPhoneyzm(String phoneyzm) {
            this.phoneyzm = phoneyzm;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getTeacherimg() {
            return teacherimg;
        }

        public void setTeacherimg(String teacherimg) {
            this.teacherimg = teacherimg;
        }

        public String getTeacherxueke() {
            return teacherxueke;
        }

        public void setTeacherxueke(String teacherxueke) {
            this.teacherxueke = teacherxueke;
        }

        public String getSchoolcode() {
            return schoolcode;
        }

        public void setSchoolcode(String schoolcode) {
            this.schoolcode = schoolcode;
        }

        public String getSchoolname() {
            return schoolname;
        }

        public void setSchoolname(String schoolname) {
            this.schoolname = schoolname;
        }

        public String getSchoolimg() {
            return schoolimg;
        }

        public void setSchoolimg(String schoolimg) {
            this.schoolimg = schoolimg;
        }

        public String getNicheng() {
            return nicheng;
        }

        public void setNicheng(String nicheng) {
            this.nicheng = nicheng;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
