package com.ssy.pink.mvp.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ssy.pink.R;
import com.ssy.pink.base.BaseActivity;
import com.ssy.pink.bean.GroupInfo;
import com.ssy.pink.common.Constants;
import com.ssy.pink.mvp.iview.IAddSmallActivityView;
import com.ssy.pink.mvp.presenter.AddSmallActivityPresenter;
import com.ssy.pink.view.dialog.CheckDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ssy
 * @date 2018/9/6
 */
public class AddSmallActivity extends BaseActivity implements IAddSmallActivityView {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.etAccout)
    EditText etAccout;

    AddSmallActivityPresenter presenter;
    GroupInfo groupInfo;
    CheckDialog checkDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_add);
        ButterKnife.bind(this);
        init();
        presenter = new AddSmallActivityPresenter(this);

    }

    private void init() {
        groupInfo = (GroupInfo) getIntent().getSerializableExtra(Constants.INTENT_KEY_DATA);
        tvTitle.setText("绑号");
    }

    @OnClick({R.id.aivBack, R.id.tvCheck, R.id.tvBind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aivBack:
                onBackPressed();
                break;
            case R.id.tvCheck:
                String str = etAccout.getText().toString().trim();
                if (TextUtils.isEmpty(str)){
                    showToast("请添加账号");
                }else{
                    presenter.checkAccout(str);
                }
                break;
            case R.id.tvBind:
                break;
        }
    }

    @Override
    public void setCheckDialog(int status) {
        if (checkDialog == null) {
            checkDialog = new CheckDialog(this);
        }
        checkDialog.setStatus(status);
        if (status == CheckDialog.STATUS_ERROR) {
            checkDialog.setErrorWord(presenter.errorWord);
        }
        checkDialog.show();
    }
}
