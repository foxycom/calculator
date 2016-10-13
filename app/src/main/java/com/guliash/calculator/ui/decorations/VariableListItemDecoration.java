package com.guliash.calculator.ui.decorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class VariableListItemDecoration extends RecyclerView.ItemDecoration {

    private float rightPadding;

    public VariableListItemDecoration(float rightPadding) {
        this.rightPadding = rightPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemCount = parent.getAdapter().getItemCount();
        int itemPos = parent.getChildAdapterPosition(view);
        Log.e("TAG", "OFFSETS " + itemPos);
        if(itemPos == itemCount - 1) {
            outRect.set(0, 0, (int)rightPadding, 0);
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }
}
