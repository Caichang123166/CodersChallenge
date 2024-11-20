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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Player_watch extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button singleModeButton, battleModeButton, playerInfoButton;
    TextView userEmail;
    String currentEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_player_watch);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //初始化 FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //取得當前用戶Email
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            currentEmail = currentUser.getEmail();
        }
        //連結UI元素
        singleModeButton = findViewById(R.id.singleMode);
        battleModeButton = findViewById(R.id.battleMode);
        playerInfoButton = findViewById(R.id.playerInfo);
        userEmail = findViewById(R.id.currentUserEmail);
        userEmail.setText(currentEmail);
    }
}