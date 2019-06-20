package com.example.johnpractice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    //宣告本頁面的物件
    Button login;
    EditText account, password;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String acnt, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 初始化
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //物件指派
        login = findViewById(R.id.btn_login);
        account = findViewById(R.id.edt_account);
        password = findViewById(R.id.edt_password);

        //button 的動作
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 取得 account 和 password
                acnt = account.getText().toString().trim();
                pass = password.getText().toString().trim();
                acnt += "@mail.fju.edu.tw";

                // 帳號密碼驗證 (Firebase) + 錯誤處理
                mAuth.signInWithEmailAndPassword(acnt, pass)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 登入成功
                                    Log.d("AUTH", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Toast.makeText(LoginActivity.this, "Success.",
                                            Toast.LENGTH_SHORT).show();

                                    //成功之後，跳轉頁面
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    // 登入失敗.
                                    Log.w("AUTH", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        }); 
                //  成功 -> 跳轉頁面
            }
        });

        //先確認使用者有沒有登入過
        if (isLogin()){
            // 已經登入 強迫登出
            mAuth.signOut();
        }
    }

    private boolean isLogin(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            return true;
        } else {
            // No user is signed in
            return false;
        }
    }
}
