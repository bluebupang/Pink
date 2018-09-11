package com.ssy.pink.mvp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ssy.pink.R;
import com.ssy.pink.adapter.SmallAdapter;
import com.ssy.pink.base.BaseActivity;
import com.ssy.pink.bean.GroupInfo;
import com.ssy.pink.bean.SmallInfo;
import com.ssy.pink.common.Constants;
import com.ssy.pink.manager.GroupManager;
import com.ssy.pink.mvp.iview.IGroupDetailActivityView;
import com.ssy.pink.mvp.presenter.GroupDetailActivityPresenter;
import com.ssy.pink.utils.ListUtils;
import com.ssy.pink.view.dialog.DeletaDialog;
import com.ssy.pink.view.recyclerViewBase.LinerRecyclerItemDecoration;
import com.ssy.pink.view.recyclerViewBase.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupDetailActivity extends BaseActivity implements IGroupDetailActivityView, SmallAdapter.OnSlideMenuListener,
        CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvRight)
    TextView tvRight;
    @BindView(R.id.cbSelectAll)
    CheckBox cbSelectAll;
    @BindView(R.id.cbAllAbnormal)
    CheckBox cbAllAbnormal;
    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.flContent)
    FrameLayout flContent;
    @BindView(R.id.tvAccoutNumber)
    TextView tvAccoutNumber;
    @BindView(R.id.ivIp)
    ImageView ivIp;
    @BindView(R.id.llMulti)
    LinearLayout llMulti;

    GroupDetailActivityPresenter presenter;
    GroupInfo groupInfo;
    SmallAdapter adapter;
    DeletaDialog deleteDialog;
    boolean isMulti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        ButterKnife.bind(this);
        init();
        presenter = new GroupDetailActivityPresenter(this).setGroupInfo(groupInfo);
        setMultiMode(false);
    }

    private void init() {
        groupInfo = (GroupInfo) getIntent().getSerializableExtra(Constants.INTENT_KEY_DATA);
        if (groupInfo == null) {
            groupInfo = new GroupInfo();
            groupInfo.setCustomergroupname("默认分组");
            groupInfo.setAllSmallInfos(GroupManager.getInstance().smallInfos);
        }
        tvTitle.setText(groupInfo.getCustomergroupname());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL, false));
        recyclerView.addItemDecoration(new LinerRecyclerItemDecoration(this, OrientationHelper.VERTICAL));
        adapter = new SmallAdapter(this, groupInfo.getAllSmallInfos());
        recyclerView.setAdapter(adapter);
        adapter.setMenuListener(this);
        tvAccoutNumber.setText(ListUtils.isEmpty(groupInfo.getAllSmallInfos()) ? "0" : String.valueOf(groupInfo.getAllSmallInfos().size()));
        cbSelectAll.setOnCheckedChangeListener(this);
        cbAllAbnormal.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.aivBack, R.id.tvRight, R.id.tvDelete, R.id.tvMove, R.id.llAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aivBack:
                onBackPressed();
                break;
            case R.id.tvRight:
                setMultiMode(!isMulti);
                break;
            case R.id.tvDelete:
                if (ListUtils.isEmpty(getSelectedList())) {
                    showToast(R.string.select_one);
                } else {
                    showMultiDeleteDialog();
                }
                break;
            case R.id.tvMove:
                break;
            case R.id.llAdd:
                Intent i = new Intent(this, AddSmallActivity.class);
                i.putExtra(Constants.INTENT_KEY_DATA, groupInfo);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.equals(cbSelectAll)) {
            if (isChecked) {
                cbAllAbnormal.setChecked(false);
            }
            presenter.selectAll(isChecked);
        } else if (buttonView.equals(cbAllAbnormal)) {
            if (isChecked) {
                cbSelectAll.setChecked(false);
                presenter.selectAbnormal();
            }

        }
    }

    @Override
    public SmallAdapter getAdapter() {
        return adapter;
    }

    private void showDeleteDialog(final SmallInfo info) {
        deleteDialog = new DeletaDialog.Builder(this)
                .setMessage("删除账号后不可恢复，需要重新添加绑定。确定要删除吗？")
                .setNegativeButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        presenter.deleteSmall(info.getWeibosmallNumId());
                    }
                })
                .create();
        deleteDialog.show();
    }

    private void showMultiDeleteDialog() {
        deleteDialog = new DeletaDialog.Builder(this)
                .setMessage("删除账号后不可恢复，需要重新添加绑定。确定要删除吗？")
                .setNegativeButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        StringBuffer sb = new StringBuffer();
                        for (SmallInfo smallInfo : getSelectedList()) {
                            sb.append(smallInfo.getWeibosmallNumId().trim()).append(";");
                        }
                        presenter.deleteSmall(sb.toString());
                    }
                })
                .create();
        deleteDialog.show();
    }

    @Override
    public void onDelete(int position) {
        showDeleteDialog(adapter.getData(position));
    }

    public void setMultiMode(boolean isMulti) {
        this.isMulti = isMulti;
        adapter.setMultiMode(isMulti);
        if (isMulti) {
            tvRight.setText(R.string.done);
            llMulti.setVisibility(View.VISIBLE);
        } else {
            tvRight.setText(R.string.manage);
            llMulti.setVisibility(View.GONE);
            presenter.selectAll(false);
        }
    }

    private List<SmallInfo> getSelectedList() {
        List<SmallInfo> selectedList = new ArrayList<>();
        for (SmallInfo smallInfo : groupInfo.getAllSmallInfos()) {
            if (smallInfo.isChecked()) {
                selectedList.add(smallInfo);
            }
        }
        return selectedList;
    }
}
