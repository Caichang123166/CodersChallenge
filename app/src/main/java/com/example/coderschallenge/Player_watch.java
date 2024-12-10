package com.example.coderschallenge;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;

import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Player_watch extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button singleModeButton, playerInfoButton;
    TextView userEmail;
    String currentEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_player_watch);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.BackButton), (v, insets) -> {
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
        playerInfoButton = findViewById(R.id.playerInfo);
        userEmail = findViewById(R.id.currentUserEmail);
        userEmail.setText(currentEmail);

        //單人模式按鈕設計
        singleModeButton.setOnClickListener(view -> {
            Intent nextStep = new Intent(Player_watch.this, chooseDifficultyAndQuestions.class);
            nextStep.putExtra("mode", "單人模式");
            startActivity(nextStep);
        });
        //玩家資訊按鈕設計
        playerInfoButton.setOnClickListener(view ->{

        });
    }
}