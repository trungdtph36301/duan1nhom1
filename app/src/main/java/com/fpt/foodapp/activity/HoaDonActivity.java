package com.fpt.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fpt.foodapp.R;
import com.fpt.foodapp.adapter.viewholder.StatusViewHolder;
import com.fpt.foodapp.common.Common;
import com.fpt.foodapp.dto.Request;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HoaDonActivity extends AppCompatActivity {


    private Toolbar tool_barHoaDon;
    private RecyclerView re_ViewListHoaDon;
    private TextView tv_TbTitle;
    private BottomNavigationView bottom_NaView;
    private ConstraintLayout cons_layoutHoaDonRoot;

    //
    FirebaseRecyclerOptions options;
    FirebaseRecyclerAdapter<Request, StatusViewHolder> adapter;
    private DatabaseReference table_GioHangDbr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don);

        cons_layoutHoaDonRoot = findViewById(R.id.cons_layoutGiohangRoot);
        re_ViewListHoaDon = findViewById(R.id.re_ViewListHoaDon);


        //
        tool_barHoaDon = findViewById(R.id.tool_barHoaDon);
        setSupportActionBar(tool_barHoaDon);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //
        tv_TbTitle = findViewById(R.id.tv_TbTitle);
        tv_TbTitle.setText("Hoá Đơn");

        //Lấy bảng Requests;
        table_GioHangDbr = FirebaseDatabase.getInstance().getReference("Requests");

        // khoi tao xoa hoa don
//        ItemTouchHelper.SimpleCallback simpleCallback = new ReViewSwipedHoaDon(0, ItemTouchHelper.LEFT, this);
//        new ItemTouchHelper(simpleCallback).attachToRecyclerView(re_ViewListHoaDon);

        //
        bottom_NaView = findViewById(R.id.bottom_NaView);
        bottom_NaView.setSelectedItemId(R.id.menu_HoaDon);
        bottom_NaView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_Home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.menu_GioHang:
                        startActivity(new Intent(getApplicationContext(), GioHangActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.menu_HoaDon:
                        break;

                    case R.id.menu_CaNhan:
                        startActivity(new Intent(getApplicationContext(), CaNhanActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });


        loadHoaDon(Common.user.getUser());
    }

    private void loadHoaDon(String user) {
        LinearLayoutManager manager = new LinearLayoutManager(HoaDonActivity.this, LinearLayoutManager.VERTICAL, false);
        re_ViewListHoaDon.setLayoutManager(manager);
        options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(table_GioHangDbr.orderByChild("user").equalTo(user), Request.class).build();
        adapter = new FirebaseRecyclerAdapter<Request, StatusViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StatusViewHolder holder, int position, @NonNull Request model) {
                holder.tv_idHoaDonItem.setText(adapter.getRef(position).getKey());
                holder.tv_statusHoaDonItem.setText(Common.coverStatus(model.getStatus()));
                holder.tv_userHoaDonItem.setText(model.getUser());
                holder.tv_addressHoaDonItem.setText(model.getAddress());
                holder.tv_phoneHoaDonItem.setText(model.getPhone());
            }

            @NonNull
            @Override
            public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_hoadon, parent, false);
                return new StatusViewHolder(view);
            }
        };


        adapter.startListening();
        re_ViewListHoaDon.setAdapter(adapter);
    }


    //Lấy status;


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if ((item.getTitle().equals(Common.DELETE))) {
            if (table_GioHangDbr.child("status").equals("0")) {
                Toast.makeText(this, "Khong the xoa", Toast.LENGTH_SHORT).show();
            }
            DeleteCatehory(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);


    }

    private void DeleteCatehory(String key) {

        table_GioHangDbr.child(key).removeValue();
    }
}
