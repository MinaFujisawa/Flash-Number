package com.example.flashnumber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private TextView scoreTextView;
    private Button retryButton;
    private int score;


    private static final String KEY_SCORE = "flashnumber_score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        // get score
        scoreTextView = (TextView) findViewById(R.id.score);
        score = getIntent().getIntExtra(KEY_SCORE, 0);
        scoreTextView.setText(String.valueOf(score));

        retryButton = (Button) findViewById(R.id.btn_retry);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
