package com.fpt.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TimActivity extends AppCompatActivity {

    MaterialSearchBar search_ViewTim;
    RecyclerView re_ViewListTim;


    //

    public FirebaseRecyclerOptions options;
    public FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>(); //Gợi ý;

    public DatabaseReference foodDbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim);


        re_ViewListTim = findViewById(R.id.re_ViewListTim);

        LinearLayoutManager manager = new LinearLayoutManager(TimActivity.this, LinearLayoutManager.VERTICAL, false);
        re_ViewListTim.setLayoutManager(manager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        re_ViewListTim.addItemDecoration(itemDecoration);
        foodDbr = FirebaseDatabase.getInstance().getReference("Foods");


        search_ViewTim = findViewById(R.id.search_ViewTim);

        search_ViewTim.setHint("Tìm món ăn");

        search_ViewTim.setLastSuggestions(suggestList); //Hiện món ăn đã tìm kiếm;
        search_ViewTim.setCardViewElevation(5); // Giới hạn hiện 10 món ăn;
        loadSuggest();
        //Người dùng nhập văn bản sẽ thay đổi danh sách đề xuất;

        search_ViewTim.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(search_ViewTim.getText().toLowerCase())) {
                        suggest.add(search);
                    }
                }
                search_ViewTim.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //
        search_ViewTim.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //Khi tìm kiếm đóng thì adapter về trạng thái ban đầu;
                if (!enabled) {
                    re_ViewListTim.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                starSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });


    }


    //Tìm món ăn theo tên;
    private void starSearch(CharSequence text) {
        //orderByChild name equalTo text.toString (so sánh tên);

        options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodDbr.orderByChild("name").equalTo(text.toString()), Food.class).build();
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.tv_nameFoodItem.setText(model.getName());
                Locale locale = new Locale("vi", "VN");
                NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
                holder.tv_tienFoodItem.setText(nf.format(Integer.parseInt(model.getPrice())));
                holder.tv_desFoodItem.setText(model.getDescription());
                Picasso.get().load(model.getImage()).into(holder.img_FoodItem);


                Food food = model;
                holder.setItemClick(new ItemClick() {
                    @Override
                    public void itemClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getApplicationContext(), "" + food.getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TimActivity.this, FoodDetailActivity.class);
                        intent.putExtra("FoodId", searchAdapter.getRef(position).getKey());
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
        searchAdapter.startListening();
        re_ViewListTim.setAdapter(searchAdapter);

    }


    //Lấy tên từ Firebase;
    private void loadSuggest() {
        foodDbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot post : snapshot.getChildren()) {
                    Food food = post.getValue(Food.class);
                    suggestList.add(food.getName());  //Lấy tên món;
                }

                search_ViewTim.setLastSuggestions(suggestList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStop() {
        if (adapter != null) {
            adapter.stopListening();
        }
        if (searchAdapter != null) {
            searchAdapter.stopListening();
        }
        super.onStop();

    }
}


