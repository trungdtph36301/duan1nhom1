package com.fpt.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fpt.foodapp.R;
import com.fpt.foodapp.database.dao.OderDetailDAO;
import com.fpt.foodapp.dto.Food;
import com.fpt.foodapp.dto.OderDetail;
import com.fpt.lib.NumberButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class FoodDetailActivity extends AppCompatActivity {


    private CollapsingToolbarLayout collapsing_ToolbarLayout;
    private ImageView img_AnhFDetail;
    private FloatingActionButton fab_CartFDetail;
    private TextView tv_nameFDetail, tv_priceFDetail, tv_desFDetail;

    DatabaseReference foodDetailDbr;

    OderDetailDAO oderDetailDAO;

    String foodId = "";

    NumberButton nb_countFDetail;
    Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //Ánh xạ;
        collapsing_ToolbarLayout = findViewById(R.id.collapsing_ToolbarLayout);
        img_AnhFDetail = findViewById(R.id.img_AnhFDetail);
        tv_nameFDetail = findViewById(R.id.tv_nameFDetail);
        tv_priceFDetail = findViewById(R.id.tv_priceFDetail);
        tv_desFDetail = findViewById(R.id.tv_desFDetail);

        nb_countFDetail = findViewById(R.id.nb_countFDetail);

        //Khởi tạo DAO;
        oderDetailDAO = new OderDetailDAO(FoodDetailActivity.this);


        //Lấy bảng Foods;
        foodDetailDbr = FirebaseDatabase.getInstance().getReference("Foods");

        //Set title;
        collapsing_ToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsing_ToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        //Lấy key ở FoodActivity;
        Intent intent = getIntent();
        if (intent != null) {
            foodId = intent.getStringExtra("FoodId");
        }
        if (!foodId.isEmpty() && foodId != null) {
            getFDetail(foodId);
        }


        fab_CartFDetail = findViewById(R.id.fab_CartFDetail);
        fab_CartFDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oderDetailDAO.insertCart(new OderDetail(
                        food.getImage(),
                        foodId,
                        food.getName(),
                        nb_countFDetail.getNumber(),
                        food.getPrice(),
                        food.getDiscount()
                ));
                Toast.makeText(FoodDetailActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFDetail(String foodId) {
        //Lấy con;
        foodDetailDbr.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                food = snapshot.getValue(Food.class);
                Picasso.get().load(food.getImage()).into(img_AnhFDetail);
                collapsing_ToolbarLayout.setTitle(food.getName());
                Locale locale = new Locale("vi", "VN");
                NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

                DecimalFormat decimalFormat = new DecimalFormat("###,###");
                String tien = decimalFormat.format(Integer.parseInt(food.getPrice()));

//                tv_priceFDetail.setText(nf.format(Integer.parseInt(food.getPrice())));
                tv_priceFDetail.setText(tien+" đ");
                tv_nameFDetail.setText(food.getName());
                tv_desFDetail.setText(food.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


}