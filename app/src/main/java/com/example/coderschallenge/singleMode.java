package com.example.coderschallenge;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class singleMode extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView current_question, question_show, question_difficulty, question_type;
    Button choice_one, choice_two, choice_three, choice_four;
    Boolean cpp, java, python, os, logic;
    int Difficulty, index=0, totalFalut=0, totalCorrect=0, cppCorrect=0, javaCorrect=0, pyCorrect=0, osCorrect=0, logicCorrect=0;
    List<Question> questionList;
    String numbersOfQuestion[] = {"第一題", "第二題", "第三題", "第四題", "第五題", "第六題", "第七題", "第八題", "第九題", "第十題"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_single_mode);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //初始化 FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //接收上個intent資訊
        Intent tmp = getIntent();
        Difficulty = tmp.getIntExtra("difficulty", 1);
        cpp = tmp.getBooleanExtra("cppCheck", true);
        java = tmp.getBooleanExtra("javaCheck", true);
        python = tmp.getBooleanExtra("pyCheck", true);
        os = tmp.getBooleanExtra("osCheck", true);
        logic = tmp.getBooleanExtra("logicCheck", true);

        //連結物件
        current_question = findViewById(R.id.currentQues);
        question_difficulty = findViewById(R.id.DifficultyShow);
        question_type = findViewById(R.id.questionTypeShow);
        question_show = findViewById(R.id.questionShow);
        choice_one = findViewById(R.id.choiceOneButton);
        choice_two = findViewById(R.id.choiceTwoButton);
        choice_three = findViewById(R.id.choiceThreeButton);
        choice_four = findViewById(R.id.choiceFourButton);

        //啟動執行緒
        new Thread(mutiThread).start();

        choice_one.setOnClickListener(view ->{
            handleAnswer(choice_one.getText().toString());
        });

        choice_two.setOnClickListener(view ->{
            handleAnswer(choice_two.getText().toString());
        });

        choice_three.setOnClickListener(view ->{
            handleAnswer(choice_three.getText().toString());
        });

        choice_four.setOnClickListener(view ->{
            handleAnswer(choice_four.getText().toString());
        });



    }
    // 初始化問題列表
    private void initializeQuestions(List<Question> questionlist) {
        questionList = questionlist;
    }

    private void handleAnswer(String selectChoice){
        if(questionList != null && !questionList.isEmpty()){
            Question currentQuestion = questionList.get(index);
            //檢查選擇是否正確
            if(selectChoice.equals(currentQuestion.getCorrectAnswer())){
                totalCorrect+=1;
                if(currentQuestion.getQuestionType().equals("c/c++")){
                    cppCorrect+=1;
                }
                if(currentQuestion.getQuestionType().equals("java")){
                    javaCorrect+=1;
                }
                if(currentQuestion.getQuestionType().equals("python")){
                    pyCorrect+=1;
                }
                if(currentQuestion.getQuestionType().equals("operating system")){
                    osCorrect+=1;
                }
                if(currentQuestion.getQuestionType().equals("logic")){
                    logicCorrect+=1;
                }
                Toast.makeText(this, "答對了！", Toast.LENGTH_SHORT).show();
            }
            else{
                totalFalut+=1;
                Toast.makeText(this, "答錯了！正確答案是：" + currentQuestion.getCorrectAnswer(), Toast.LENGTH_SHORT).show();
            }
            index+=1;

            // 檢查是否已經到達問題列表的末尾
            if(index >= questionList.size()) {
                // 顯示最終成績頁面
                Intent intent = new Intent(this, finalOfSingleMode.class);
                intent.putExtra("totalCorrect", totalCorrect);
                intent.putExtra("totalFault", totalFalut);
                intent.putExtra("cppCorrect", cppCorrect);
                intent.putExtra("javaCorrect", javaCorrect);
                intent.putExtra("pyCorrect", pyCorrect);
                intent.putExtra("osCorrect", osCorrect);
                intent.putExtra("logicCorrect", logicCorrect);
                intent.putExtra("cpp", cpp);
                intent.putExtra("java", java);
                intent.putExtra("python", python);
                intent.putExtra("os", os);
                intent.putExtra("logic", logic);
                intent.putExtra("difficulty", Difficulty);

                startActivity(intent);
                finish();
            }
            else{
                updateQuestionUI(questionList); // 更新UI到下一題
            }
        }
    }

    // 建立一個執行緒執行的事件取得網路資料
    private Runnable mutiThread = new Runnable() {
        public void run() {
            try {
                // 使用您的實際伺服器IP或網域
                String urlString = "http://172.20.10.3/GetData.php" +
                        "?difficulty=" + Difficulty +
                        "&cpp=" + cpp +
                        "&java=" + java +
                        "&python=" + python +
                        "&os=" + os +
                        "&logic=" + logic;
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(false);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.connect();

                int responseCode = connection.getResponseCode();
                Log.d("NetworkDebug", "Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                    StringBuilder box = new StringBuilder();
                    String line;
                    while ((line = bufReader.readLine()) != null) {
                        box.append(line).append("\n");
                    }
                    inputStream.close();

                    // 打印獲取的JSON數據
                    Log.d("NetworkDebug", "Received JSON: " + box.toString());

                    final List<Question> questionList = parseQuesions(box.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initializeQuestions(questionList);
                            if (!questionList.isEmpty()) {
                                updateQuestionUI(questionList); // 確保問題列表不為空
                            }
                        }
                    });
                } else {
                    Log.e("NetworkDebug", "HTTP request failed with response code: " + responseCode);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handleNetworkError();
                        }
                    });
                }
            } catch (Exception e) {
                Log.e("NetworkDebug", "Network Error", e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleNetworkError();
                    }
                });
            }
        }
    };
    // Question 類別
    public class Question {
        private String questionType;
        private String difficulty;
        private String questionText;
        private String choiceOne;
        private String choiceTwo;
        private String choiceThree;
        private String choiceFour;
        private String correctAnswer;

        public Question(String questionType, String difficulty, String questionText,
                        String choiceOne, String choiceTwo, String choiceThree,
                        String choiceFour, String correctAnswer) {
            this.questionType = questionType;
            this.difficulty = difficulty;
            this.questionText = questionText;
            this.choiceOne = choiceOne;
            this.choiceTwo = choiceTwo;
            this.choiceThree = choiceThree;
            this.choiceFour = choiceFour;
            this.correctAnswer = correctAnswer;
        }
        // Getters
        public String getQuestionType() { return questionType; }
        public String getDifficulty() { return difficulty; }
        public String getQuestionText() { return questionText; }
        public String getChoiceOne() { return choiceOne; }
        public String getChoiceTwo() { return choiceTwo; }
        public String getChoiceThree() { return choiceThree; }
        public String getChoiceFour() { return choiceFour; }
        public String getCorrectAnswer() { return correctAnswer; }
    }

    // 解析JSON的方法
    private List<Question> parseQuesions(String jsonString){
        List<Question> questions = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(jsonString);
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Question question = new Question(
                        jsonObject.getString("questiontype"),
                        jsonObject.getString("difficulty"),
                        jsonObject.getString("question"),
                        jsonObject.getString("choiceone"),
                        jsonObject.getString("choicetwo"),
                        jsonObject.getString("choicethree"),
                        jsonObject.getString("choicefour"),
                        jsonObject.getString("answer")
                );
                questions.add(question);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return questions;
    }

    // 更新UI的方法
    private void updateQuestionUI(List<Question> questions){
        if(!questions.isEmpty()){
            Question currentQuestion = questions.get(index);
            //設置題目文字and選項
            current_question.setText(numbersOfQuestion[index]);
            question_show.setText(currentQuestion.getQuestionText());
            question_difficulty.setText(currentQuestion.getDifficulty());
            question_type.setText(currentQuestion.getQuestionType());
            choice_one.setText(currentQuestion.getChoiceOne());
            choice_two.setText(currentQuestion.getChoiceTwo());
            choice_three.setText(currentQuestion.getChoiceThree());
            choice_four.setText(currentQuestion.getChoiceFour());
        }
    }

    // 處理網絡錯誤的方法
    private void handleNetworkError() {
        // 顯示錯誤提示
        Toast.makeText(this, "網絡連接失敗，請檢查您的網絡設置", Toast.LENGTH_SHORT).show();
    }
}