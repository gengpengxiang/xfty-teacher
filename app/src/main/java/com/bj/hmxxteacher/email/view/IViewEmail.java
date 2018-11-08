package com.bj.hmxxteacher.email.view;

import com.bj.hmxxteacher.email.model.Letter;
import com.bj.hmxxteacher.email.model.Reply;
import com.bj.hmxxteacher.mvp.MvpView;

import java.util.List;

/**
 * Created by Administrator on 2018/10/29 0029.
 */

public interface IViewEmail extends MvpView{

    void getEmailList(String emailInfo);
    void xingbiao(String result);
    void delete(String result);

}
