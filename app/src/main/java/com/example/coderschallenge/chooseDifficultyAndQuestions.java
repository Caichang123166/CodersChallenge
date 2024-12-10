package com.example.coderschallenge;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class chooseDifficultyAndQuestions extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button backButton, easyButton, mediumButton, hardButton, startTestButton;
    CheckBox OBcpp, OBjava, OBpython, OBoperatingSystem, OBlogic;
    TextView currentMode, currentDifficulty;
    //設置一個變數代表玩家所選難度, 1=easy, 2=medium, 3=hard, 預設=1
    int playerChooseDifficulty = 1;
    //設置五個Bool值, 代表是否選擇了此科目, 預設皆為未選擇
    boolean cpp = false, java = false, python = false, operatingSystem = false, logic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_difficulty_and_questions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.CL), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //初始化 FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //接收上個intent資訊
        Intent tmp = getIntent();
        //連結物件
        backButton = findViewById(R.id.backButton);
        easyButton = findViewById(R.id.DifficultyEasyButton);
        mediumButton = findViewById(R.id.DifficultyMediumButton);
        hardButton = findViewById(R.id.DifficultyHardButton);
        startTestButton = findViewById(R.id.startTest);
        currentMode = findViewById(R.id.CurrentMode);
        currentDifficulty = findViewById(R.id.CurrentDifficulty);
        OBcpp = findViewById(R.id.chooseCpp);
        OBjava = findViewById(R.id.chooseJava);
        OBpython = findViewById(R.id.choosePython);
        OBoperatingSystem = findViewById(R.id.chooseOS);
        OBlogic = findViewById(R.id.chooseLogic);

        //設定當前模式顯示文字
        currentMode.setText(tmp.getStringExtra("mode"));

        //設置開始測驗按鈕---傳遞難易度以及題目種類
        startTestButton.setOnClickListener(view->{
            Intent intent = new Intent(chooseDifficultyAndQuestions.this, singleMode.class);
            intent.putExtra("difficulty", playerChooseDifficulty);
            intent.putExtra("cppCheck", cpp);
            intent.putExtra("javaCheck", java);
            intent.putExtra("pyCheck", python);
            intent.putExtra("osCheck", operatingSystem);
            intent.putExtra("logicCheck", logic);
            startActivity(intent);
            finish();
        });

        //設置easy、medium、hard、back Button
        easyButton.setOnClickListener(view -> {
            currentDifficulty.setText("簡單模式");
            playerChooseDifficulty = 1;
        });
        mediumButton.setOnClickListener(view -> {
            currentDifficulty.setText("中等模式");
            playerChooseDifficulty = 2;
        });
        hardButton.setOnClickListener(view -> {
            currentDifficulty.setText("困難模式");
            playerChooseDifficulty = 3;
        });
        backButton.setOnClickListener(view -> {
            finish();
        });

        //設置選擇題目的checkBox
        OBcpp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cpp = isChecked;
            }
        });
        OBjava.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                java = isChecked;
            }
        });
        OBpython.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                python = isChecked;
            }
        });
        OBoperatingSystem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                operatingSystem = isChecked;
            }
        });
        OBlogic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                logic = isChecked;
            }
        });
    }
}