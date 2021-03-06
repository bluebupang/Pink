package com.ssy.pink.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssy.greendao.helper.HelperFactory;
import com.ssy.greendao.helper.SmallStatusDbHelper;
import com.ssy.pink.R;
import com.ssy.pink.base.BaseRecycleViewAdapter;
import com.ssy.pink.bean.SmallInfo;
import com.ssy.pink.mvp.activity.MyIdolActivity;
import com.ssy.pink.utils.ListUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ssy
 * @date 2018/8/17
 */
public class SmallAdapter extends BaseRecycleViewAdapter<SmallInfo> {

    private boolean isMulti;
    private OnSlideMenuListener menuListener;
    SmallStatusDbHelper dbHelper = HelperFactory.getSmallStatusDbHelper();

    public SmallAdapter(Context context, List<SmallInfo> data) {
        super(context, data);
    }

    public void setMenuListener(OnSlideMenuListener menuListener) {
        this.menuListener = menuListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupRecycleViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_small, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SmallInfo info = data.get(position);
        GroupRecycleViewHolder myHolder = (GroupRecycleViewHolder) holder;
        myHolder.tvWeiboUid.setText(info.getWeibosmallNumId());
        if (TextUtils.isEmpty(info.getSmallWeiboNum())) {
            myHolder.tvWeiboAccout.setVisibility(View.GONE);
        } else {
            myHolder.tvWeiboAccout.setText(info.getSmallWeiboNum());
            myHolder.tvWeiboAccout.setVisibility(View.VISIBLE);
        }

        myHolder.tvStatus.setText(dbHelper.uniqueQuery(info.getWeibosmallNumId()).getNormal() ? "有效" : "无效");
        if (isMulti) {
            myHolder.checkbox.setVisibility(View.VISIBLE);
            myHolder.checkbox.setChecked(info.isChecked());
        } else {
            myHolder.checkbox.setVisibility(View.GONE);
        }
    }

    class GroupRecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tvWeiboUid)
        TextView tvWeiboUid;
        @BindView(R.id.ivIcon)
        ImageView ivIcon;
        @BindView(R.id.menuDelete)
        TextView menuDelete;
        @BindView(R.id.tvWeiboAccout)
        TextView tvWeiboAccout;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.checkbox)
        CheckBox checkbox;

        public GroupRecycleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    data.get(getAdapterPosition()).setChecked(isChecked);
                }
            });
        }

        @OnClick({R.id.menuDelete})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.menuDelete:
                    if (menuListener != null) {
                        menuListener.onDelete(getAdapterPosition());
                    }
                    break;
                default:
                    onClick(view);
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            if (itemClickListen != null) {
                itemClickListen.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnSlideMenuListener {
        void onDelete(int position);
    }

    public void setMultiMode(boolean isMulti) {
        this.isMulti = isMulti;
        notifyDataSetChanged();
    }
}
