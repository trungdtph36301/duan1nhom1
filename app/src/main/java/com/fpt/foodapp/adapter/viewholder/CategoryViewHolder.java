package com.fpt.foodapp.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fpt.foodapp.interfaces.ItemClick;
import com.fpt.foodapp.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public ImageView img_CateItem;
    public TextView tv_CateItem;
    public ItemClick itemClick;


    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        img_CateItem = itemView.findViewById(R.id.img_CateItem);
        tv_CateItem = itemView.findViewById(R.id.tv_CateItem);
        itemView.setOnClickListener(this);

    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public void onClick(View view) {
        itemClick.itemClick(view, getLayoutPosition(), false);
    }
}
