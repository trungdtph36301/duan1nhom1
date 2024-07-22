package com.fpt.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.fpt.foodapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CaNhanActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar tool_barCaiDat;
    private BottomNavigationView bottom_NaView;
    private TextView tv_TbTitle, tv_CnTlTk, tv_CnViTien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ca_nhan);

        //Ánh xa;
        tool_barCaiDat = findViewById(R.id.tool_barCaiDat);
        tv_TbTitle = findViewById(R.id.tv_TbTitle);
        tv_CnTlTk = findViewById(R.id.tv_CnTlTk);
        tv_CnViTien = findViewById(R.id.tv_CnViTien);

        //Title hiện lên toolbar;
        setSupportActionBar(tool_barCaiDat);
        tv_TbTitle.setText("Cá nhân");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //
        bottom_NaView = findViewById(R.id.bottom_NaView);
        bottom_NaView.setSelectedItemId(R.id.menu_CaNhan);
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
                        startActivity(new Intent(getApplicationContext(), HoaDonActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.menu_CaNhan:
                        break;

                }
                return true;
            }
        });


        tv_CnTlTk.setOnClickListener(this);
        tv_CnViTien.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_CnTlTk:
                startActivity(new Intent(getApplicationContext(), ThietLapTKActivity.class));

                break;

            case R.id.tv_CnViTien:
                startActivity(new Intent(getApplicationContext(), ViTienActivity.class));
                break;
        }
    }

}