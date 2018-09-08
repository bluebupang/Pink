package com.ssy.pink.mvp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.ssy.pink.R;
import com.ssy.pink.adapter.GroupAdapter;
import com.ssy.pink.base.BaseActivity;
import com.ssy.pink.base.BaseRecycleViewAdapter;
import com.ssy.pink.bean.GroupInfo;
import com.ssy.pink.common.Constants;
import com.ssy.pink.common.EventCode;
import com.ssy.pink.mvp.iview.IGroupActivityView;
import com.ssy.pink.manager.GroupManager;
import com.ssy.pink.mvp.presenter.GroupActivityPresenter;
import com.ssy.pink.utils.ListUtils;
import com.ssy.pink.view.dialog.DeletaDialog;
import com.ssy.pink.view.recyclerViewBase.SpaceItemDecoration;
import com.ssy.pink.view.recyclerViewBase.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupActivity extends BaseActivity implements IGroupActivityView {

    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.flContent)
    FrameLayout flContent;
    @BindView(R.id.llSlideTips)
    LinearLayout llSlideTips;
    @BindView(R.id.tvNull)
    TextView tvNull;

    private GroupActivityPresenter presenter;
    private GroupAdapter adapter;
    private DeletaDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);
        init();
        initListener();
        presenter = new GroupActivityPresenter(this);
    }

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL, false));
        recyclerView.addItemDecoration(new SpaceItemDecoration());

        adapter = new GroupAdapter(this, GroupManager.getInstance().groupInfos);
        recyclerView.setAdapter(adapter);
        setNullTipsVisible();
    }

    private void initListener() {
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                presenter.listGroup();
            }
        });
        adapter.setItemClickListen(new BaseRecycleViewAdapter.OnItemClickListen() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(GroupActivity.this, GroupDetailActivity.class);
                intent.putExtra(Constants.INTENT_KEY_DATA, adapter.getData(position));
                startActivity(intent);
            }
        });
        adapter.setMenuListener(new GroupAdapter.OnSlideMenuListener() {
            @Override
            public void onEdit(int position) {
                Intent intent = new Intent(GroupActivity.this, GroupEditActivity.class);
                intent.putExtra(Constants.INTENT_KEY_DATA, adapter.getData(position));
                startActivity(intent);
            }

            @Override
            public void onDelete(int position) {
                showDeleteDialog(adapter.getData(position));
            }
        });
    }


    @OnClick({R.id.aivBack, R.id.aivAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aivBack:
                onBackPressed();
                break;
            case R.id.aivAdd:
                startActivity(new Intent(GroupActivity.this, GroupAddActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(EventCode.UPDATE_GROUPS);
        super.onBackPressed();
    }

    private void setNullTipsVisible() {
        if (ListUtils.isEmpty(adapter.getDatas())) {
            llSlideTips.setVisibility(View.GONE);
            tvNull.setVisibility(View.VISIBLE);
        } else {
            llSlideTips.setVisibility(View.VISIBLE);
            tvNull.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadGroups() {
        adapter.notifyDataSetChanged();
        setNullTipsVisible();
    }

    @Override
    public void finishRefresh() {
        refreshLayout.finishRefresh();
    }

    private void showDeleteDialog(final GroupInfo info) {
        deleteDialog = new DeletaDialog.Builder(this)
                .setMessage("所删除的分组包含账号，删除后账号将回归默认分组，确定继续吗？")
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
                        presenter.deleteGroup(info);
                    }
                })
                .create();
        deleteDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(Integer eventCode) {
        switch (eventCode) {
            case EventCode.ADD_GROUP:
                adapter.notifyDataSetChanged();
                setNullTipsVisible();
                break;
            case EventCode.EDIT_GROUP:
                adapter.notifyDataSetChanged();
                break;
        }
    }
}