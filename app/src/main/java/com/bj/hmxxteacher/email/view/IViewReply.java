package com.bj.hmxxteacher.email.view;

import com.bj.hmxxteacher.email.model.Letter;
import com.bj.hmxxteacher.email.model.Reply;
import com.bj.hmxxteacher.mvp.MvpView;

/**
 * Created by Administrator on 2018/10/29 0029.
 */

public interface IViewReply extends MvpView{

    void getEmailReply(String reply);
    void sendReply(String result);

}
