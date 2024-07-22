package com.fpt.foodapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fpt.foodapp.R;
import com.fpt.foodapp.common.Common;
import com.fpt.foodapp.dto.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText edt_loginUser, edt_loginPass;
    private TextInputLayout text_inputLayoutLoginUser, text_inputLayoutLoginPass;
    private CheckBox ckb_loginRemember;
    private Button btn_loginLogin;
    private TextView tv_loginRegister, tv_nameLog;
    private DatabaseReference table_UserDbr;
    final String USER_SAVE = "User_Save"; ///Để lưu tài khoản mật khẩu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //
        edt_loginUser = findViewById(R.id.edt_loginUser);
        edt_loginPass = findViewById(R.id.edt_loginPass);
        ckb_loginRemember = findViewById(R.id.ckb_loginRemember);
        btn_loginLogin = findViewById(R.id.btn_loginLogin);
        tv_loginRegister = findViewById(R.id.tv_loginRegister);
        text_inputLayoutLoginUser = findViewById(R.id.text_inputLayoutLoginUser);
        text_inputLayoutLoginPass = findViewById(R.id.text_inputLayoutLoginPass);
        //


        //Lấy bảng User;
        table_UserDbr = FirebaseDatabase.getInstance().getReference("User");


        //Tạo  SharedPreferences để ghi nhớ;
        SharedPreferences spf = getSharedPreferences(USER_SAVE, MODE_PRIVATE);
        String user_save = spf.getString("user_sv", "");
        String pass_save = spf.getString("pass_sv", "");
        Boolean ckb_save = spf.getBoolean("re_sve", false);

        //Set vào SharedPreferences;
        edt_loginUser.setText(user_save);
        edt_loginPass.setText(pass_save);
        ckb_loginRemember.setChecked(ckb_save);


        btn_loginLogin.setOnClickListener(this);
        tv_loginRegister.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_loginLogin:
                loginUser();
                break;
            case R.id.tv_loginRegister:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }


    //Validate
    private Boolean valUser() {
        String valUser = text_inputLayoutLoginUser.getEditText().getText().toString().trim();
        String check = "\\A\\w{4,20}\\z";
        if (valUser.isEmpty()) {
            text_inputLayoutLoginUser.setError("Không được bỏ trống");
            return false;
        } else if (!valUser.matches(check)) {
            text_inputLayoutLoginUser.setError("Tên tài khoản không có khoản trắng");
            return false;

        } else {
            text_inputLayoutLoginUser.setError(null);
            text_inputLayoutLoginUser.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean valPass() {
        String valPass = text_inputLayoutLoginPass.getEditText().getText().toString().trim();
        if (valPass.isEmpty()) {
            text_inputLayoutLoginPass.setError("Không được bỏ trống");
            return false;
        } else {
            text_inputLayoutLoginPass.setError(null);
            text_inputLayoutLoginPass.setErrorEnabled(false);
            return true;
        }
    }

    //Đăng nhập;
    private void loginUser() {
        String loginUser = text_inputLayoutLoginUser.getEditText().getText().toString().trim();
        String loginPass = text_inputLayoutLoginPass.getEditText().getText().toString().trim();


        table_UserDbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Lấy key user để xét tài khoản có hay chưa;
                if (!valUser() | !valPass()) {
                    return;
                } else {
                    if (snapshot.child(loginUser).exists()) {
                        //Lấy từ key user để lấy pass;
                        User user = snapshot.child(loginUser).getValue(User.class);
                        user.setUser(loginUser); //set User;
                        if (Boolean.parseBoolean(user.getIsUser()) == true) {
                            if (user.getPass().equalsIgnoreCase(loginPass)) {
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                remember_user(loginUser, loginPass, ckb_loginRemember.isChecked());
                                //Intent;
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Common.user = user;  //Lấy tên đăng nhập;
                                startActivity(intent);
                                finish();
                                table_UserDbr.removeEventListener(this);
                            } else {
                                Toast.makeText(LoginActivity.this, "Tài khoản hoặc Mật khẩu sai\n           Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    //Check remember;
    private void remember_user(String u, String p, boolean check) {
        SharedPreferences spf = getSharedPreferences(USER_SAVE, MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        if (!check) {
            //Xoá tính trạng lưu trước đó;
            editor.clear();
        } else {
            //Lưu tình trạng;
            editor.putString("user_sv", u);
            editor.putString("pass_sv", p);
            editor.putBoolean("re_sve", check);

        }
        editor.commit();
    }


}