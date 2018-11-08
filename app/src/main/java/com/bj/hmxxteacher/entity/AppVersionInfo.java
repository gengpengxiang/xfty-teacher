package com.bj.hmxxteacher.entity;

/**
 * Created by zz379 on 2017/3/15.
 */

public class AppVersionInfo {

    private String errorCode;
    private String message;

    private String title;
    private String content;
    private String downloadUrl;

    private String qiangzhigengxin;

    public String getQiangzhigengxin() {
        return qiangzhigengxin;
    }

    public void setQiangzhigengxin(String qiangzhigengxin) {
        this.qiangzhigengxin = qiangzhigengxin;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "AppVersionInfo{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + message + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
