package com.example.coderschallenge;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText emailEditText, passwordEditText;
    Button registerComplete, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.BackButton), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //初始化 FirebaseAuth and Realtime database
        mAuth = FirebaseAuth.getInstance();

        //link
        emailEditText = findViewById(R.id.editTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextPassword);
        registerComplete = findViewById(R.id.registerComplete);
        backButton = findViewById(R.id.back_button);


        //setOnClickListener
        registerComplete.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(RegisterActivity.this, "帳號或密碼不得為空!", Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser(email, password);
        });

        backButton.setOnClickListener(view->{
            finish();
        });

    }

    //用戶註冊
    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task->{
            if(task.isSuccessful()){
                //註冊成功
                FirebaseUser user = mAuth.getCurrentUser();
                Toast.makeText(RegisterActivity.this, "註冊成功!", Toast.LENGTH_SHORT).show();
                if (user != null) {
                    sendUserDataToServer(user.getEmail());
                }
                finish();
            }
            else{
                //註冊失敗
                Toast.makeText(RegisterActivity.this, "註冊失敗!: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 將用戶資料發送到伺服器
    private void sendUserDataToServer(String email) {
        String url = "http://172.20.10.3/add_player.php"; // 替換為你的伺服器 URL

        // 初始化 RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // 建立 POST 請求
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // 處理伺服器回應
                    if (response.equals("success")) {
                        Toast.makeText(RegisterActivity.this, "玩家資訊新增成功!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "伺服器錯誤: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(RegisterActivity.this, "網路錯誤: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("adminemail", email);
                params.put("totaldone", "0");
                params.put("cppcorrectrate", "0.0");
                params.put("javacorrectrate", "0.0");
                params.put("pycorrectrate", "0.0");
                params.put("oscorrectrate", "0.0");
                params.put("logiccorrectrate", "0.0");
                return params;
            }
        };

        // 將請求添加到請求隊列中
        requestQueue.add(stringRequest);
    }
}