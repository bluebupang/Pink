package com.ssy.pink.view.recyclerViewBase;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ssy on 2017/9/19.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space = 25;
    private boolean haveTopSpace;

    public SpaceItemDecoration() {
    }

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    public SpaceItemDecoration(boolean haveTopSpace) {
        this.haveTopSpace = haveTopSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (haveTopSpace) {
            outRect.top = space;
        } else {
            if (parent.getChildPosition(view) != 0) {
                outRect.top = space;
            }
        }

    }
}
