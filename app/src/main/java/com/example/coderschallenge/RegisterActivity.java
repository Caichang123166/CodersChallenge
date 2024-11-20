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

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //初始化 FirebaseAuth
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
                finish();
            }
            else{
                //註冊失敗
                Toast.makeText(RegisterActivity.this, "註冊失敗!: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}