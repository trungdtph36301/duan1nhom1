package com.fpt.foodapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fpt.foodapp.R;
import com.fpt.foodapp.adapter.adapter.GioHangAdapter;
import com.fpt.foodapp.adapter.viewholder.GioHangViewHolder;
import com.fpt.foodapp.common.Common;
import com.fpt.foodapp.database.dao.OderDetailDAO;
import com.fpt.foodapp.dto.OderDetail;
import com.fpt.foodapp.dto.Request;
import com.fpt.foodapp.interfaces.ReViewXoa;
import com.fpt.foodapp.swiped.ReViewSwiped;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GioHangActivity extends AppCompatActivity implements View.OnClickListener, ReViewXoa {

    private Toolbar tool_barCuaHang;
    private RecyclerView re_ViewListGioHang;
    private TextView tv_TbTitle, tv_moneyGioHang;
    private BottomNavigationView bottom_NaView;
    private Button btn_DatHang;
    private ConstraintLayout cons_layoutGiohangRoot;

    DatabaseReference GioHangDbr;

    ArrayList<OderDetail> listOderDetails = new ArrayList<>();

    GioHangAdapter gioHangAdapter;

    OderDetailDAO oderDetailDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        re_ViewListGioHang = findViewById(R.id.re_ViewListGioHang);
        cons_layoutGiohangRoot = findViewById(R.id.cons_layoutGiohangRoot);

        tv_moneyGioHang = findViewById(R.id.tv_moneyGioHang);
        //
        tool_barCuaHang = findViewById(R.id.tool_barCuaHang);
        setSupportActionBar(tool_barCuaHang);
        tv_TbTitle = findViewById(R.id.tv_TbTitle);
        tv_TbTitle.setText("Giỏ hàng");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        oderDetailDAO = new OderDetailDAO(this);

        //Tạo bảng Giỏ Hàng;
        GioHangDbr = FirebaseDatabase.getInstance().getReference("Requests");


        // Khởi tạo Swiped xoá;
        ItemTouchHelper.SimpleCallback simpleCallback = new ReViewSwiped(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(re_ViewListGioHang);

        //
        bottom_NaView = findViewById(R.id.bottom_NaView);
        bottom_NaView.setSelectedItemId(R.id.menu_GioHang);
        bottom_NaView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_Home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.menu_GioHang:
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

        //Set hiện thị theo hướng dọc;
        resetData();
        LinearLayoutManager manager = new LinearLayoutManager(GioHangActivity.this, LinearLayoutManager.VERTICAL, false);
        re_ViewListGioHang.setLayoutManager(manager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        re_ViewListGioHang.addItemDecoration(itemDecoration);

        //Hiện giỏ hàng;
        loadGioHang();

        //
        btn_DatHang = findViewById(R.id.btn_DatHang);
        btn_DatHang.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_DatHang:
                if (listOderDetails.size() > 0) {
                    int tong = 0;
                    for (OderDetail oderDetail : listOderDetails)
                        tong = tong + (Integer.parseInt(oderDetail.getPrice())) * (Integer.parseInt(oderDetail.getQuanTity()));
                    int finalTong = tong;
                    if (Common.user.getVitien() >= finalTong) {
                        DatHang();
                    } else {
                        Toast.makeText(this, "   Ví tiền không đủ ! \nVui lòng nạp thêm tiền", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    KoCoSp();
                }
                break;
        }
    }

    //
    private void KoCoSp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GioHangActivity.this);
        builder.setTitle("Thông báo !!!");
        builder.setMessage("Không có sản phẩm ? Vui lòng mua sản phẩm");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(GioHangActivity.this, MainActivity.class));
                finish();
                dialog.dismiss();
            }
        });

        builder.show();
    }


    //
    private void DatHang() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GioHangActivity.this);
        builder.setTitle("Thông báo !!!");
        builder.setMessage("Thêm địa chỉ ?");

        //Tạo edt_address;
        EditText edt_address = new EditText(this);

        //Set chiều dài,chiều rộng;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        edt_address.setLayoutParams(layoutParams);
        builder.setView(edt_address);
        builder.setIcon(R.drawable.ic_shop_black);


        //
        int tong = 0;
        for (OderDetail oderDetail : listOderDetails)
            tong += (Integer.parseInt(oderDetail.getPrice())) * (Integer.parseInt(oderDetail.getQuanTity()));
        int finalTong = tong;

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (edt_address.getText().toString().isEmpty()) {


                }else {
                    Map<String, Object> passtien = new HashMap<>();
                    passtien.put("vitien", Common.user.getVitien() - finalTong);
                    DatabaseReference table_userDbr = FirebaseDatabase.getInstance().getReference("User");
                    table_userDbr.child(Common.user.getUser()).updateChildren(passtien).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(GioHangActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                    //Tạo mới;
                    Request request = new Request(Common.user.getUser(), Common.user.getPhone(), edt_address.getText().toString(), tv_moneyGioHang.getText().toString(), listOderDetails);
                    //Thêm vào bảng Requests;
                    GioHangDbr.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                    //Mua hàng xoá hết sản phẩm;
                    oderDetailDAO.deleteCart();
                    Toast.makeText(GioHangActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(GioHangActivity.this, HoaDonActivity.class));
                }

            }

        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        resetData();
    }


    //Lấy tất cả hiện lên RecyclerView ;
    private void resetData() {
        listOderDetails = (ArrayList<OderDetail>) oderDetailDAO.getAll();
        gioHangAdapter = new GioHangAdapter(listOderDetails, this);
        re_ViewListGioHang.setAdapter(gioHangAdapter);
    }

    //Hiện giỏ hàng;
    private void loadGioHang() {
        int tong = 0;
        for (OderDetail oderDetail : listOderDetails)
            tong = tong + (Integer.parseInt(oderDetail.getPrice())) * (Integer.parseInt(oderDetail.getQuanTity()));
        Locale locale = new Locale("vi", "VN");
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String tien = decimalFormat.format(tong);

        //tv_moneyGioHang.setText(nf.format(tien));
        tv_moneyGioHang.setText(tien + " đ");

    }


    //Kéo sang trái khoá;
    @Override
    public void itemSwiped(RecyclerView.ViewHolder viewHolder, int direction, int id) {
        if (viewHolder instanceof GioHangViewHolder) {
            //Lấy tên sản phẩm;
            String name = ((GioHangAdapter) re_ViewListGioHang.getAdapter()).getItem(viewHolder.getLayoutPosition()).getProductName();
            OderDetail deleteItem = ((GioHangAdapter) re_ViewListGioHang.getAdapter()).getItem(viewHolder.getLayoutPosition());
            int deleteIndex = viewHolder.getLayoutPosition();

            gioHangAdapter.xoa(deleteIndex);
            oderDetailDAO.deleteId(deleteItem.getProductId());
            loadGioHang();

            //Hoàn tác;
            Snackbar snackbar = Snackbar.make(cons_layoutGiohangRoot, name + " xoá thành công", Snackbar.LENGTH_SHORT);
            snackbar.setAction("Hoàn tác", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oderDetailDAO.insertCart(deleteItem);
                    gioHangAdapter.hoantac(deleteItem, deleteIndex);
                    loadGioHang();
                }
            });

            snackbar.setActionTextColor(Color.WHITE);
            snackbar.show();


        }
    }
}