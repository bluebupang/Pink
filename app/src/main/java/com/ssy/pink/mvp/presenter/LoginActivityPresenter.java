package com.ssy.pink.mvp.presenter;

import com.ssy.pink.MyApplication;
import com.ssy.pink.base.BasePresenter;
import com.ssy.pink.bean.FansOrgInfo;
import com.ssy.pink.bean.WeiboCustomerInfo;
import com.ssy.pink.bean.WeiboUserInfo;
import com.ssy.pink.bean.response.CommonListResp;
import com.ssy.pink.bean.response.CommonResp;
import com.ssy.pink.bean.response.WeiboErrorResp;
import com.ssy.pink.common.ResponseCode;
import com.ssy.pink.manager.UserManager;
import com.ssy.pink.manager.WeiboManager;
import com.ssy.pink.mvp.iview.ILoginActivityView;
import com.ssy.pink.network.api.PinkNet;
import com.ssy.pink.network.api.WeiboNet;
import com.ssy.pink.utils.JsonUtils;
import com.ssy.pink.utils.MyUtils;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class LoginActivityPresenter extends BasePresenter {
    private ILoginActivityView iView;

    public LoginActivityPresenter(ILoginActivityView iView) {
        this.iView = iView;
    }

    public void listFansOrg() {

        PinkNet.listFansOrg(new Subscriber<CommonListResp<FansOrgInfo>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                MyUtils.handleExcep(e);
                iView.showToast("组织获取失败");
                iView.hasGotOrgs(false);
            }

            @Override
            public void onNext(CommonListResp commonListResp) {
                iView.hasGotOrgs(true);
                iView.setOrgsList(commonListResp.getData());

            }
        });

    }

    public void getWeiboUserInfo(final String weiboNum, final String fansOrgNum) {
        WeiboNet.getUserInfo(WeiboManager.getInstance().mAccessToken.getUid(), WeiboManager.getInstance().mAccessToken.getToken(),
                new Subscriber<WeiboUserInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showProgress(false);
                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException) e;
                            try {
                                String errorMsg = exception.response().errorBody().string();
                                WeiboErrorResp errorResp = JsonUtils.toObject(errorMsg, WeiboErrorResp.class);
                                iView.showToast(errorResp.getError());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                                iView.showToast(e.getMessage());
                            }
                        } else {
                            iView.showToast(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(WeiboUserInfo weiboUserInfo) {
                        WeiboManager.getInstance().userInfo = weiboUserInfo;
                        syncCustomer(WeiboManager.getInstance().mAccessToken.getUid(), weiboNum, weiboUserInfo.getName(), fansOrgNum);
                    }
                });
    }

    /**
     * 同步主账号信息
     */
    public void syncCustomer(String weiboId, String weiboNum, String weiboName, String fansOrgNum) {
        PinkNet.syncCustomer(weiboId, weiboNum, weiboName, fansOrgNum, new Subscriber<CommonResp<WeiboCustomerInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                MyUtils.handleExcep(e);
            }

            @Override
            public void onNext(CommonResp<WeiboCustomerInfo> resp) {
                if (resp.getCode().equalsIgnoreCase(ResponseCode.CODE_SUCCESS)) {
                    UserManager.getInstance().userInfo = resp.getData();
                    MyApplication.getInstance().setToken(resp.getData().getSessionid());
                    iView.toMainActivity();
                } else {
                    iView.showToast(resp.getMsg());
                    iView.showProgress(false);
                }

            }
        });
    }
}
