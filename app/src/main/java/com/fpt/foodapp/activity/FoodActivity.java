package com.fpt.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fpt.foodapp.R;
import com.fpt.foodapp.adapter.viewholder.FoodViewHolder;
import com.fpt.foodapp.dto.Food;
import com.fpt.foodapp.interfaces.ItemClick;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class FoodActivity extends AppCompatActivity {


    public RecyclerView re_Food;
    public FirebaseRecyclerOptions options;
    public FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    public DatabaseReference foodDbr;

    public static String categoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        re_Food = findViewById(R.id.re_Food);

        //Tạo bảng Foods;
        foodDbr = FirebaseDatabase.getInstance().getReference("Foods");


        //Tạo layout theo chiều dọc;
        LinearLayoutManager manager = new LinearLayoutManager(FoodActivity.this, LinearLayoutManager.VERTICAL, false);
        re_Food.setLayoutManager(manager);

        //Tạo ngăn;
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        re_Food.addItemDecoration(itemDecoration);
        //Lấy key;
        Intent intent = getIntent();
        if (intent != null) {
            categoryId = intent.getStringExtra("CategoryId");
        }


        if (!categoryId.isEmpty() && categoryId != null) {
            loadFood(categoryId);
        }


    }

    private void loadFood(String categoryId) {


        options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodDbr.orderByChild("menuid").equalTo(categoryId), Food.class).build();
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.tv_nameFoodItem.setText(model.getName());


                Locale locale = new Locale("vi", "VN");
                NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

                DecimalFormat decimalFormat = new DecimalFormat("###,###");
                String tien = decimalFormat.format(Integer.parseInt(model.getPrice()));

//                holder.tv_tienFoodItem.setText(nf.format(Integer.parseInt(model.getPrice())));
                holder.tv_tienFoodItem.setText(tien+" đ");
                holder.tv_desFoodItem.setText(model.getDescription());
                Picasso.get().load(model.getImage()).into(holder.img_FoodItem);

                Food food = model;

                holder.setItemClick(new ItemClick() {
                    @Override
                    public void itemClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(FoodActivity.this, "" + food.getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FoodActivity.this, FoodDetailActivity.class);
                        intent.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_food, parent, false);
                return new FoodViewHolder(view);
            }
        };


        adapter.startListening();
        Log.d("====", "" + adapter.getItemCount());
        re_Food.setAdapter(adapter);
    }

}