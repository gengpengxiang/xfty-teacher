package com.bj.hmxxteacher.grade.view;

import com.bj.hmxxteacher.mvp.MvpView;

/**
 * Created by Administrator on 2018/11/1 0001.
 */

public interface IViewGrade extends MvpView{

    void getPositions(String result);
    void getGrades(String result);
    void getClassList(String result);
}
