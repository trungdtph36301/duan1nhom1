package com.fpt.foodapp.swiped;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.fpt.foodapp.adapter.viewholder.GioHangViewHolder;
import com.fpt.foodapp.interfaces.ReViewXoa;

public class ReViewSwiped extends ItemTouchHelper.SimpleCallback {

    private ReViewXoa reViewXoa;

    public ReViewSwiped(int dragDirs, int swipeDirs, ReViewXoa reViewXoa) {
        super(dragDirs, swipeDirs);
        this.reViewXoa = reViewXoa;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        if (reViewXoa != null) {
            reViewXoa.itemSwiped(viewHolder, direction, viewHolder.getLayoutPosition());
        }
    }


    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        View cons_layoutGioHang = ((GioHangViewHolder) viewHolder).cons_layoutGioHang;
        getDefaultUIUtil().clearView(cons_layoutGioHang);

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View cons_layoutGioHang = ((GioHangViewHolder) viewHolder).cons_layoutGioHang;
        getDefaultUIUtil().onDraw(c, recyclerView, cons_layoutGioHang, dX, dY, actionState, isCurrentlyActive);

    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (viewHolder != null) {
            View cons_layoutGioHang = ((GioHangViewHolder) viewHolder).cons_layoutGioHang;
            getDefaultUIUtil().onSelected(cons_layoutGioHang);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View cons_layoutGioHang = ((GioHangViewHolder) viewHolder).cons_layoutGioHang;
        getDefaultUIUtil().onDrawOver(c, recyclerView, cons_layoutGioHang, dX, dY, actionState, isCurrentlyActive);

    }
}