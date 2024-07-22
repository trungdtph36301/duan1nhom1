package com.fpt.foodapp.adapter.viewholder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fpt.foodapp.R;
import com.fpt.foodapp.common.Common;
import com.fpt.foodapp.interfaces.ItemClick;

public class StatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

    public TextView tv_idHoaDonItem, tv_statusHoaDonItem, tv_userHoaDonItem, tv_phoneHoaDonItem, tv_addressHoaDonItem;
    private ItemClick itemClick;

    public RelativeLayout re_layoutDeleteHoaDon;
    public ConstraintLayout cons_layoutHoaDon;

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public StatusViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_idHoaDonItem = itemView.findViewById(R.id.tv_idHoaDonItem);
        tv_statusHoaDonItem = itemView.findViewById(R.id.tv_statusHoaDonItem);
        tv_userHoaDonItem = itemView.findViewById(R.id.tv_userHoaDonItem);
        tv_phoneHoaDonItem = itemView.findViewById(R.id.tv_phoneHoaDonItem);
        tv_addressHoaDonItem = itemView.findViewById(R.id.tv_addressHoaDonItem);
        re_layoutDeleteHoaDon = itemView.findViewById(R.id.re_layoutDeleteHoaDon);
        cons_layoutHoaDon = itemView.findViewById(R.id.cons_layoutHoaDon);

//        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClick.itemClick(v, getLayoutPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(0,0,getAdapterPosition(), Common.DELETE);
    }
}
