package com.example.coderschallenge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class finalOfSingleMode extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView chooseDifficulty, chooseTypes, totalCorrect, totalFault, correctRate, comments;
    Button checkout;
    String Difficulty = "", types = "", comment = "";
    int correctRates=0, corrects=0, faults=0, cppCorrect=0, javaCorrect=0, pyCorrect=0, osCorrect=0, logicCorrect=0;
    int difficultyTmp;
    Boolean cpptmp, javatmp, pytmp, ostmp, logictmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_final_of_single_mode);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //初始化 FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //連結物件
        chooseDifficulty = findViewById(R.id.ChooseDiffText);
        chooseTypes = findViewById(R.id.ChooseTypeText);
        totalCorrect = findViewById(R.id.totalCorrect);
        totalFault = findViewById(R.id.totalFault);
        correctRate = findViewById(R.id.correctRate);
        comments = findViewById(R.id.comments);
        checkout = findViewById(R.id.checkoutButton);

        //接收上個intent資訊
        Intent tmp = getIntent();

        difficultyTmp = tmp.getIntExtra("difficulty", 1);
        cpptmp = tmp.getBooleanExtra("cpp", false);
        javatmp = tmp.getBooleanExtra("java", false);
        pytmp = tmp.getBooleanExtra("python", false);
        ostmp = tmp.getBooleanExtra("os", false);
        logictmp = tmp.getBooleanExtra("logic", false);
        corrects = tmp.getIntExtra("totalCorrect", 0);
        faults = tmp.getIntExtra("totalFault", 1);
        cppCorrect = tmp.getIntExtra("cppCorrect", 0);
        javaCorrect = tmp.getIntExtra("javaCorrect", 0);
        pyCorrect = tmp.getIntExtra("pyCorrect", 0);
        osCorrect = tmp.getIntExtra("osCorrect", 0);
        logicCorrect = tmp.getIntExtra("logicCorrect", 0);
        //判斷difficulty
        if(difficultyTmp == 1){
            Difficulty = "簡單";
        }
        else if(difficultyTmp == 2){
            Difficulty = "中等";
        }
        else if(difficultyTmp == 3){
            Difficulty = "困難";
        }
        //判斷所選題目種類
        if(cpptmp){
            types = "c/c++";
        }
        if(javatmp){
            types+= ", java";
        }
        if(pytmp){
            types+= ", python";
        }
        if(ostmp){
            types+= ", OS";
        }
        if(logictmp){
            types+= ", logic";
        }
        //計算錯誤率
        correctRates = corrects;
        updateUI();

        //在此更新玩家資訊至資料庫
        checkout.setOnClickListener(view -> {
            updatePlayerStats(corrects, faults, correctRates, types);
            finish();
        });
    }

    private void updateUI(){
        chooseDifficulty.setText("難度: " + Difficulty);
        chooseTypes.setText("題目種類: " + types);
        totalCorrect.setText("總答對題數: " + corrects);
        totalFault.setText("總答錯題數: " + faults);
        correctRate.setText("答對率: " + corrects*10 + "%");
        //處理評語
        if(corrects>=8 && corrects<=10){
            comment = "很好, 繼續保持";
        }
        else if(corrects >=6 && corrects <8){
            comment = "有及格, 不錯";
        }
        else if(corrects >=4 && corrects<6){
            comment = "快及格了";
        }
        else if(corrects>=0 && corrects<4){
            comment = "你必修怎麼過的???";
        }
        comments.setText("評語 : " + comment);
    }

    // 更新玩家資料至資料庫
    private void updatePlayerStats(int corrects, int faults, int correctRate, String types) {
        String url = "http://172.20.10.3/update_player.php"; // 替換為你的伺服器 URL

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equals("success")) {
                        Toast.makeText(finalOfSingleMode.this, "資料更新成功!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(finalOfSingleMode.this, "伺服器錯誤: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(finalOfSingleMode.this, "網路錯誤: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("adminemail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                params.put("totaldone", String.valueOf(corrects + faults));
                params.put("cppcorrectrate", cpptmp ? String.valueOf(cppCorrect) : "0.0");
                params.put("javacorrectrate", javatmp ? String.valueOf(javaCorrect) : "0.0");
                params.put("pycorrectrate", pytmp ? String.valueOf(pyCorrect) : "0.0");
                params.put("oscorrectrate", ostmp ? String.valueOf(osCorrect) : "0.0");
                params.put("logiccorrectrate", logictmp ? String.valueOf(logicCorrect) : "0.0");
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}