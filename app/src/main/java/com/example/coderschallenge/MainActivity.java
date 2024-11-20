package com.example.coderschallenge;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText emailEditText, passwordEditText;
    Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //初始化 FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //連結 UI元素
        emailEditText = findViewById(R.id.Username);
        passwordEditText = findViewById(R.id.Password);
        loginButton = findViewById(R.id.Login);
        registerButton = findViewById(R.id.Register);

        //註冊按鈕事件
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        //登入按鈕事件
        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(MainActivity.this, "帳號或密碼不得為空!", Toast.LENGTH_SHORT).show();
                return;
            }
            loginUser(email, password);
        });
    }

    //用戶登入
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task->{
            //登入成功
            if(task.isSuccessful()){
                FirebaseUser user = mAuth.getCurrentUser();
                Toast.makeText(MainActivity.this, "登入成功!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Player_watch.class);
                startActivity(intent);
            }
            else{
                //登入失敗
                Toast.makeText(MainActivity.this, "登入失敗!: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}