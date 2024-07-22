package com.fpt.foodapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fpt.foodapp.R;
import com.fpt.foodapp.Service.ListOders;
import com.fpt.foodapp.adapter.viewholder.CategoryViewHolder;
import com.fpt.foodapp.dto.Banner;
import com.fpt.foodapp.dto.Category;
import com.fpt.foodapp.dto.User;
import com.fpt.foodapp.interfaces.ItemClick;
import com.fpt.library.SliderLayout;
import com.fpt.library.animations.DescriptionAnimation;
import com.fpt.library.slidertypes.BaseSliderView;
import com.fpt.library.slidertypes.TextSliderView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private RecyclerView re_Cate;
    private TextView tv_User, tv_wCome, tv_ThucDon, tv_searchView, tv_SoTien;
    private BottomNavigationView bottom_NaView;
    //
    private FirebaseRecyclerOptions options;
    private FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;
    private DatabaseReference cateDbr;
    //Banner;
    private DatabaseReference bannerDbr;
    private DatabaseReference table_userDbr;
    private SliderLayout slider_Layout;
    private HashMap<String, String> img_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        // khoi chay service
        Intent intent = new Intent(MainActivity.this, ListOders.class);
        startService(intent);
        re_Cate = findViewById(R.id.re_Cate);

        //Chọn item;
        bottom_NaView = findViewById(R.id.bottom_NaView);
        bottom_NaView.setSelectedItemId(R.id.menu_Home);
        bottom_NaView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_Home:
                        break;

                    case R.id.menu_GioHang:
                        startActivity(new Intent(getApplicationContext(), GioHangActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.menu_HoaDon:
                        startActivity(new Intent(getApplicationContext(), HoaDonActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.menu_CaNhan:
                        startActivity(new Intent(getApplicationContext(), CaNhanActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                }
                return true;
            }
        });

        //Tạo bảng Category;
        cateDbr = FirebaseDatabase.getInstance().getReference("Category");
        loadCate();

        //Lấy tên đăng nhập;
        tv_User = findViewById(R.id.tv_User);
        //   tv_User.setText(Common.user.getUser());


        //Set fonts;
        tv_ThucDon = findViewById(R.id.tv_ThucDon);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        tv_ThucDon.setTypeface(typeface);


        //Bảng User;
        table_userDbr = FirebaseDatabase.getInstance().getReference("User");
        //Hiện tiền;
        tv_SoTien = findViewById(R.id.tv_SoTien);
        table_userDbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot a : snapshot.getChildren()) {
                    User user = a.getValue(User.class);

                    DecimalFormat decimalFormat = new DecimalFormat("###,###");
                    String tien = decimalFormat.format(user.getVitien());
//                    Locale locale = new Locale("vi", "VN");
//                    NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
//                    tv_SoTien.setText(nf.format(Integer.parseInt(tien)));



                    tv_SoTien.setText(tien+" đ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Tìm kiếm;
        tv_searchView = findViewById(R.id.tv_searchView);
        tv_searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TimActivity.class);
                startActivity(intent);
            }
        });


        //Banner;
        setupBanner();

    }

    private void setupBanner() {
        slider_Layout = findViewById(R.id.slider_Layout);
        img_list = new HashMap<>();
        bannerDbr = FirebaseDatabase.getInstance().getReference("Banner");

        bannerDbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot a : snapshot.getChildren()) {
                    Banner banner = a.getValue(Banner.class);
                    img_list.put(banner.getName() + "_" + banner.getId(), banner.getImage());
                }
                for (String key : img_list.keySet()) {
                    String[] keyList = key.split("_");
                    String nameOf = keyList[0];
                    String idOf = keyList[1];


                    //Tạo slider;
                    TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                    textSliderView.description(nameOf).image(img_list.get(key)).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            Intent intent = new Intent(MainActivity.this, FoodDetailActivity.class);
                            intent.putExtras(textSliderView.getBundle());
                            startActivity(intent);
                        }
                    });

                    //Thêm extra vào Bundle;
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("FoodId", idOf);


                    slider_Layout.addSlider(textSliderView);

                    bannerDbr.removeEventListener(this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        slider_Layout.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        slider_Layout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider_Layout.setCustomAnimation(new DescriptionAnimation());
        slider_Layout.setDuration(3000);

    }

    @Override
    protected void onStop() {
        super.onStop();
        slider_Layout.stopAutoCycle();
    }

    //Lấy từ Thực đơn từ Firebase;
    private void loadCate() {
        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 2);
        re_Cate.setLayoutManager(manager);

        options = new FirebaseRecyclerOptions.Builder<Category>().setQuery(cateDbr, Category.class).build();
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull Category model) {
                holder.tv_CateItem.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.img_CateItem);
                Category category = model;
                holder.setItemClick(new ItemClick() {
                    @Override
                    public void itemClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(MainActivity.this, "" + category.getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, FoodActivity.class);
                        //Tạo key Category;
                        intent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_cate, parent, false);
                return new CategoryViewHolder(view);
            }
        };
        adapter.startListening();
        re_Cate.setAdapter(adapter);
    }

    //Xuất hiện từng chữ;


}