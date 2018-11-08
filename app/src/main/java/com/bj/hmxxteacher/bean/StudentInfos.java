package com.bj.hmxxteacher.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/11/3 0003.
 */

public class StudentInfos {
    /**
     * ret : 1
     * msg :
     * data : {"score":132,"dianzan":"0","gaijin":"1","class_score":"92","huizhang":"7","baogao_title":"2018年球季学期报告","baogao_status":"1","dengji":"幸福二星","sum":"13","pingyu":"金，最近很积极哦，给你点个赞！","updatetime":"2018-11-03 16:19:31","zhuanxiang":"0","chongwu_status":1,"chongwu":{"chongwu_info":{"id":"1","name":"西西","img":"chongwu/1.png","updatetime":"2018-11-03 09:52:18","img_s":"chongwu/1.png","grade":"1","shuoming":"说明1"},"chongwu_guli":[{"id":"1","content":"好好学习天天向上","chongwu_id":"1","updatetime":"2018-07-23 15:32:51"},{"id":"2","content":"人生要有理想","chongwu_id":"1","updatetime":"2018-07-23 15:33:10"},{"id":"3","content":"梅花香自苦寒来","chongwu_id":"1","updatetime":"2018-07-24 16:32:18"},{"id":"4","content":"拥有梦想\\n只是一种智力，\\n实现梦想\\n才是一种能力。","chongwu_id":"1","updatetime":"2018-07-25 16:30:57"}]}}
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
         * score : 132
         * dianzan : 0
         * gaijin : 1
         * class_score : 92
         * huizhang : 7
         * baogao_title : 2018年球季学期报告
         * baogao_status : 1
         * dengji : 幸福二星
         * sum : 13
         * pingyu : 金，最近很积极哦，给你点个赞！
         * updatetime : 2018-11-03 16:19:31
         * zhuanxiang : 0
         * chongwu_status : 1
         * chongwu : {"chongwu_info":{"id":"1","name":"西西","img":"chongwu/1.png","updatetime":"2018-11-03 09:52:18","img_s":"chongwu/1.png","grade":"1","shuoming":"说明1"},"chongwu_guli":[{"id":"1","content":"好好学习天天向上","chongwu_id":"1","updatetime":"2018-07-23 15:32:51"},{"id":"2","content":"人生要有理想","chongwu_id":"1","updatetime":"2018-07-23 15:33:10"},{"id":"3","content":"梅花香自苦寒来","chongwu_id":"1","updatetime":"2018-07-24 16:32:18"},{"id":"4","content":"拥有梦想\\n只是一种智力，\\n实现梦想\\n才是一种能力。","chongwu_id":"1","updatetime":"2018-07-25 16:30:57"}]}
         */

        private int score;
        private String dianzan;
        private String gaijin;
        private String class_score;
        private String huizhang;
        private String baogao_title;
        private String baogao_status;
        private String dengji;
        private String sum;
        private String pingyu;
        private String updatetime;
        private String zhuanxiang;
        private int chongwu_status;
        private ChongwuBean chongwu;

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
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

        public String getClass_score() {
            return class_score;
        }

        public void setClass_score(String class_score) {
            this.class_score = class_score;
        }

        public String getHuizhang() {
            return huizhang;
        }

        public void setHuizhang(String huizhang) {
            this.huizhang = huizhang;
        }

        public String getBaogao_title() {
            return baogao_title;
        }

        public void setBaogao_title(String baogao_title) {
            this.baogao_title = baogao_title;
        }

        public String getBaogao_status() {
            return baogao_status;
        }

        public void setBaogao_status(String baogao_status) {
            this.baogao_status = baogao_status;
        }

        public String getDengji() {
            return dengji;
        }

        public void setDengji(String dengji) {
            this.dengji = dengji;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getPingyu() {
            return pingyu;
        }

        public void setPingyu(String pingyu) {
            this.pingyu = pingyu;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getZhuanxiang() {
            return zhuanxiang;
        }

        public void setZhuanxiang(String zhuanxiang) {
            this.zhuanxiang = zhuanxiang;
        }

        public int getChongwu_status() {
            return chongwu_status;
        }

        public void setChongwu_status(int chongwu_status) {
            this.chongwu_status = chongwu_status;
        }

        public ChongwuBean getChongwu() {
            return chongwu;
        }

        public void setChongwu(ChongwuBean chongwu) {
            this.chongwu = chongwu;
        }

        public static class ChongwuBean {
            /**
             * chongwu_info : {"id":"1","name":"西西","img":"chongwu/1.png","updatetime":"2018-11-03 09:52:18","img_s":"chongwu/1.png","grade":"1","shuoming":"说明1"}
             * chongwu_guli : [{"id":"1","content":"好好学习天天向上","chongwu_id":"1","updatetime":"2018-07-23 15:32:51"},{"id":"2","content":"人生要有理想","chongwu_id":"1","updatetime":"2018-07-23 15:33:10"},{"id":"3","content":"梅花香自苦寒来","chongwu_id":"1","updatetime":"2018-07-24 16:32:18"},{"id":"4","content":"拥有梦想\\n只是一种智力，\\n实现梦想\\n才是一种能力。","chongwu_id":"1","updatetime":"2018-07-25 16:30:57"}]
             */

            private ChongwuInfoBean chongwu_info;
            private List<ChongwuGuliBean> chongwu_guli;

            public ChongwuInfoBean getChongwu_info() {
                return chongwu_info;
            }

            public void setChongwu_info(ChongwuInfoBean chongwu_info) {
                this.chongwu_info = chongwu_info;
            }

            public List<ChongwuGuliBean> getChongwu_guli() {
                return chongwu_guli;
            }

            public void setChongwu_guli(List<ChongwuGuliBean> chongwu_guli) {
                this.chongwu_guli = chongwu_guli;
            }

            public static class ChongwuInfoBean {
                /**
                 * id : 1
                 * name : 西西
                 * img : chongwu/1.png
                 * updatetime : 2018-11-03 09:52:18
                 * img_s : chongwu/1.png
                 * grade : 1
                 * shuoming : 说明1
                 */

                private String id;
                private String name;
                private String img;
                private String updatetime;
                private String img_s;
                private String grade;
                private String shuoming;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getUpdatetime() {
                    return updatetime;
                }

                public void setUpdatetime(String updatetime) {
                    this.updatetime = updatetime;
                }

                public String getImg_s() {
                    return img_s;
                }

                public void setImg_s(String img_s) {
                    this.img_s = img_s;
                }

                public String getGrade() {
                    return grade;
                }

                public void setGrade(String grade) {
                    this.grade = grade;
                }

                public String getShuoming() {
                    return shuoming;
                }

                public void setShuoming(String shuoming) {
                    this.shuoming = shuoming;
                }
            }

            public static class ChongwuGuliBean {
                /**
                 * id : 1
                 * content : 好好学习天天向上
                 * chongwu_id : 1
                 * updatetime : 2018-07-23 15:32:51
                 */

                private String id;
                private String content;
                private String chongwu_id;
                private String updatetime;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getChongwu_id() {
                    return chongwu_id;
                }

                public void setChongwu_id(String chongwu_id) {
                    this.chongwu_id = chongwu_id;
                }

                public String getUpdatetime() {
                    return updatetime;
                }

                public void setUpdatetime(String updatetime) {
                    this.updatetime = updatetime;
                }
            }
        }
    }
}
