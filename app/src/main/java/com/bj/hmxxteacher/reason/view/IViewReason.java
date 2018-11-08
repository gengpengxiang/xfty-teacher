package com.bj.hmxxteacher.reason.view;


import com.bj.hmxxteacher.mvp.MvpView;
import com.bj.hmxxteacher.reason.model.Reason;

import java.util.List;

public interface IViewReason extends MvpView {

    void getReasonInfo(String result);

    void comment(String result);
}
