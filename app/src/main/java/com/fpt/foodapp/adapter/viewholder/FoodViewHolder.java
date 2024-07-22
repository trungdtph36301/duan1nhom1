package com.fpt.foodapp.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fpt.foodapp.interfaces.ItemClick;
import com.fpt.foodapp.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView img_FoodItem;
    public TextView tv_nameFoodItem, tv_tienFoodItem, tv_desFoodItem;
    ItemClick itemClick;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        img_FoodItem = itemView.findViewById(R.id.img_FoodItem);
        tv_nameFoodItem = itemView.findViewById(R.id.tv_nameFoodItem);
        tv_tienFoodItem = itemView.findViewById(R.id.tv_TienFoodItem);
        tv_desFoodItem = itemView.findViewById(R.id.tv_desFoodItem);
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
