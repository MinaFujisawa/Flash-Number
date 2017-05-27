package com.example.flashnumber;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Random random = new Random();
    private NumButton numButton;
    private int clickNum = 4;
    private TextView mCountDownTextView;
    private int[] setNumList;
    private int[] setNumListOrdered;
    private ArrayList<Button> btnList;
    private int clicked = 0;
    private static final int MAX_NUM = 15;


    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        int i = 0;

        //get btn num index
        int length = String.valueOf(b).length();
        String btnIndexString = String.valueOf(b);
        int btnIndex = Integer.parseInt(String.valueOf(b).substring(length - 2, length - 1)) - 1;

        //check the answers
        if (clicked + 1 == clickNum) {
            b.setText(String.valueOf(setNumListOrdered[clicked]));
            clickNum++;
            loadActivity();
        } else {
            if (setNumList[btnIndex] == (setNumListOrdered[clicked])) {
                b.setText(String.valueOf(setNumListOrdered[clicked]));
            } else {
                Toast.makeText(MainActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
            }
        }
        clicked++;

        i++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadActivity();
    }

    private void loadActivity() {
        numButton = new NumButton(this);

        //switch layouts
        switch (clickNum) {
            case 4:
                setContentView(R.layout.num_four);
                break;
            case 5:
                setContentView(R.layout.num_five);
                break;
        }



        setNumList = new int[clickNum];
        setNumListOrdered = new int[clickNum];

        // add click listeners for the buttons
        for (int i = 0; i < clickNum; i++) {
            numButton.getBtnList(clickNum).get(i).setOnClickListener(this);
        }


        //Count down
        mCountDownTextView = (TextView) findViewById(R.id.countDown);
        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {
                mCountDownTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                mCountDownTextView.setText("");
                setNum(clickNum);
                //invisible number
                new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        for (int i = 0; i < clickNum; i++) {
                            numButton.getBtnList(clickNum).get(i).setText("");
                            numButton.getBtnList(clickNum).get(i).setEnabled(true);
                        }
                    }
                }.start();
            }
        }.start();



    }




    public void setNum(int clickNum) {
        boolean num[] = new boolean[MAX_NUM];
        int i = 0;
        while (i < clickNum) {
            int p = random.nextInt(MAX_NUM);
            if (num[p] == false) {
                numButton.getBtnList(clickNum).get(i).setText(String.valueOf(p));
                setNumListOrdered[i] = p;
                setNumList[i] = p;
                num[p] = true;
                i++;
            }
        }
        Arrays.sort(setNumListOrdered);

    }

    private View.OnClickListener ReloadActivity = new View.OnClickListener() {
        public void onClick(View v) {
            loadActivity();
        }
    };


}
