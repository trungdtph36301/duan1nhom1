package com.fpt.foodapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.fpt.foodapp.R;
import com.fpt.foodapp.common.Common;
import com.fpt.foodapp.dto.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ViTienActivity extends AppCompatActivity {
    private Toolbar tool_barViTien;
    private TextView tv_TbTitle, tv_SoDuViTien;
    private TextInputEditText edt_NapViTien;
    private AppCompatButton btn_NapViTien;


    private DatabaseReference table_userDbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_tien);

        edt_NapViTien = findViewById(R.id.edt_NapViTien);


        //
        tv_TbTitle = findViewById(R.id.tv_TbTitle);
        tv_TbTitle.setText("Nạp ");
        tool_barViTien = findViewById(R.id.tool_barViTien);
        setSupportActionBar(tool_barViTien);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Bảng User;
        table_userDbr = FirebaseDatabase.getInstance().getReference("User");

        //
        tv_SoDuViTien = findViewById(R.id.tv_SoDuViTien);
        table_userDbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot a : snapshot.getChildren()) {
                    User user = a.getValue(User.class);


                    DecimalFormat decimalFormat = new DecimalFormat("###,###");
                    String tien = decimalFormat.format(user.getVitien());

//                    Locale locale = new Locale("vi", "VN");
//                    NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
//                    tv_SoDuViTien.setText(nf.format(Integer.parseInt(tien)));


                    tv_SoDuViTien.setText(tien+" đ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //
        btn_NapViTien = findViewById(R.id.btn_NapViTien);
        btn_NapViTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                napTien();
            }
        });


    }


    private void napTien() {
        Map<String, Object> nap = new HashMap<>();
        int nhapTien = Integer.parseInt(edt_NapViTien.getText().toString());
        nap.put("vitien", Double.parseDouble(String.valueOf(nhapTien)) + Common.user.getVitien());

        table_userDbr.child(Common.user.getUser()).updateChildren(nap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ViTienActivity.this, "Nạp thành công", Toast.LENGTH_SHORT).show();
                edt_NapViTien.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViTienActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}