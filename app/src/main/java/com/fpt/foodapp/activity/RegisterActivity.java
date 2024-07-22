package com.fpt.foodapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fpt.foodapp.R;
import com.fpt.foodapp.dto.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText edt_registerName, edt_registerPhone, edt_registerUser, edt_registerPass;
    private TextInputLayout text_inputLayoutName, text_inputLayoutPhone, text_inputLayoutUser, text_inputLayoutPass;


    private Button btn_registerRegister;
    private TextView tv_CoTk;


    private DatabaseReference userDbr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        //
        edt_registerName = findViewById(R.id.edt_registerName);
        edt_registerPhone = findViewById(R.id.edt_registerPhone);
        edt_registerUser = findViewById(R.id.edt_registerUser);
        edt_registerPass = findViewById(R.id.edt_registerPass);


        text_inputLayoutName = findViewById(R.id.text_inputLayoutName);
        text_inputLayoutPhone = findViewById(R.id.text_inputLayoutPhone);
        text_inputLayoutUser = findViewById(R.id.text_inputLayoutUser);
        text_inputLayoutPass = findViewById(R.id.text_inputLayoutPass);


        tv_CoTk = findViewById(R.id.tv_CoTk);

        btn_registerRegister = findViewById(R.id.btn_registerRegister);


        //Tạo bảng User;
        userDbr = FirebaseDatabase.getInstance().getReference("User");

        //
        btn_registerRegister.setOnClickListener(this);
        tv_CoTk.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_registerRegister:
                insertUser();
                break;

            case R.id.tv_CoTk:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
        }

    }


    //Validate;
    private Boolean valName() {
        String valName = text_inputLayoutName.getEditText().getText().toString().trim();


        if (valName.isEmpty()) {
            text_inputLayoutName.setError("Không được để trống.");
            return false;
        } else if (valName.length() > 18) {
            text_inputLayoutName.setError("Tên quá dài.");
            return false;
        } else if (valName.length() < 10) {
            text_inputLayoutName.setError("Tên quá ngắn.");
            return false;
        } else {

            text_inputLayoutName.setError(null);
            text_inputLayoutName.setErrorEnabled(false);
            return true;
        }


    }


    private Boolean valPhone() {
        String valPhone = text_inputLayoutPhone.getEditText().getText().toString().trim();

        if (valPhone.isEmpty()) {
            text_inputLayoutPhone.setError("Không được để trống.");
            return false;


        } else if (valPhone.length() > 10  || valPhone.length() < 10 ) {
            text_inputLayoutPhone.setError("Số điện thoại có 10 số.");
            return false;

        } else if (!ischeckPhone(valPhone)) {
            text_inputLayoutPhone.setError("Số điện thoại là số.");
            return false;

        } else {
            text_inputLayoutPhone.setError(null);
            text_inputLayoutPhone.setErrorEnabled(false);
            return true;
        }
    }

    //Check số điện thoại là số;
    public Boolean ischeckPhone(String phone) {
        try {
            Integer.parseInt(phone);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private Boolean valUser() {
        String valUser = text_inputLayoutUser.getEditText().getText().toString().trim();
        String check = "\\A\\w{4,20}\\z";
        if (valUser.isEmpty()) {
            text_inputLayoutUser.setError("Không được để trống");
            return false;
        } else if (valUser.length() > 15) {
            text_inputLayoutUser.setError("Tên đăng nhập quá dài");
            return false;
        } else if (!valUser.matches(check)) {
            text_inputLayoutUser.setError("Tên đăng nhập không có khoảng trắng");
            return false;
        } else {
            text_inputLayoutUser.setError(null);
            text_inputLayoutUser.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean valPass() {
        String valPass = text_inputLayoutPass.getEditText().getText().toString().trim();
        String check = "^" + "(?=.*[a-zA-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{4,}" + "$";
        if (valPass.isEmpty()) {
            text_inputLayoutPass.setError("Không được bỏ trống");
            return false;
        } else if (!valPass.matches(check)) {
            text_inputLayoutPass.setError("Mật khẩu quá yếu");
            return false;
        } else {
            text_inputLayoutPass.setError(null);
            text_inputLayoutPass.setErrorEnabled(false);
            return true;
        }
    }

    //Thêm;
    private void insertUser() {
        String registerName = text_inputLayoutName.getEditText().getText().toString().trim();
        String registerPhone = text_inputLayoutPhone.getEditText().getText().toString().trim();
        String registerUser = text_inputLayoutUser.getEditText().getText().toString().trim();
        String registerPass = text_inputLayoutPass.getEditText().getText().toString().trim();

        //Validate;
        if (!valName() | !valPhone() | !valUser() | !valPass()) {
            return;

        } else {
            //Thêm vào bảng User vào Firebase;
            userDbr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (snapshot.child(registerUser).exists()) {
                        Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        edt_registerUser.setText("");
                    } else {
                        User user = new User(registerName, registerUser, registerPass, registerPhone);
                        user.getIsUser();
                        userDbr.child(registerUser).setValue(user); //Tạo key;
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        edt_registerName.setText("");
                        edt_registerPhone.setText("");
                        edt_registerUser.setText("");
                        edt_registerPass.setText("");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }

}