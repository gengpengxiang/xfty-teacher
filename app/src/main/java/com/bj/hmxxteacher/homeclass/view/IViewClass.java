package com.bj.hmxxteacher.homeclass.view;

import com.bj.hmxxteacher.mvp.MvpView;

/**
 * Created by Administrator on 2018/11/1 0001.
 */

public interface IViewClass extends MvpView{

    void getClassList(String result);
    void getClassInfo(String result);
    void getClassDynamic(String result);
    void getTeacherInfo(String result);

}
