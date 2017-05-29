package com.example.flashnumber;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Random random = new Random();
    private static final int MIN_STAGE = 4;
    private static final int MAX_NUM = 15;
    private int stageNum = MIN_STAGE;
    private TextView mCountDownTextView;
    private TextView mDescriptionTextView;
    private int[] setNumList;
    private int[] setNumListOrdered;
    private ArrayList<Button> btnList;
    private int countClicked;
    Dialog settingsDialog;


    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        int i = 0;

        //get btn num index
        int length = String.valueOf(b).length();
        int btnIndex = Integer.parseInt(String.valueOf(b).substring(length - 2, length - 1)) - 1;

        //check the answers
        if (countClicked + 1 != stageNum) {
            //correct
            if (setNumList[btnIndex] == (setNumListOrdered[countClicked])) {
                b.setText(String.valueOf(setNumListOrdered[countClicked]));
                setNumbersStyle(btnIndex);
                countClicked++;
                //incorrect
            } else {
                showImage(false);
                for (int j = 0; j < stageNum; j++) {
                    if (((String) btnList.get(j).getText()).equals("")) {
                        btnList.get(j).setText(String.valueOf(setNumList[j]));
                        btnList.get(j).setTextColor(getResources().getColor(R.color.incorrect));
                    }
                }

                // back to the previous stage
                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        if (stageNum != MIN_STAGE) {
                            stageNum--;
                        }
                        loadActivity();
                    }
                }.start();
            }
            // all correct
        } else {
            showImage(true);
            b.setText(String.valueOf(setNumListOrdered[countClicked]));
            stageNum++;
            new CountDownTimer(500, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    loadActivity();
                }
            }.start();
        }
        i++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadActivity();
    }

    private void loadActivity() {

        //switch layouts
        switch (stageNum) {
            case 4:
                setContentView(R.layout.num_four);
                resetItems();
                break;
            case 5:
                setContentView(R.layout.num_five);
                resetItems();
                break;
        }

        settingsDialog = new Dialog(this, R.style.TransparentDialogTheme);

        //add Buttons
        btnList.add((Button) findViewById(R.id.btn1));
        btnList.add((Button) findViewById(R.id.btn2));
        btnList.add((Button) findViewById(R.id.btn3));
        btnList.add((Button) findViewById(R.id.btn4));
        btnList.add((Button) findViewById(R.id.btn5));


        // add click listeners for the buttons
        for (int i = 0; i < stageNum; i++) {
            btnList.get(i).setOnClickListener(this);
            btnList.get(i).setEnabled(false);
        }

        mDescriptionTextView = (TextView) findViewById(R.id.description);
        mDescriptionTextView.setText(R.string.description1);


        //Count down
        mCountDownTextView = (TextView) findViewById(R.id.countDown);
        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {
                mCountDownTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                mCountDownTextView.setText("");
                setNum(stageNum);
                //invisible number
                new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        for (int i = 0; i < stageNum; i++) {
                            btnList.get(i).setText("");
                            btnList.get(i).setEnabled(true);
                            btnList.get(i).setBackgroundResource(R.drawable.clicable_buttton_bg);
                            btnList.get(i).setWidth(55);
                            btnList.get(i).setHeight(55);
                            btnList.get(i).setTextSize(45);
                            mDescriptionTextView.setText(R.string.description2);

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
                btnList.get(i).setText(String.valueOf(p));
                setNumListOrdered[i] = p;
                setNumList[i] = p;
                num[p] = true;

                //style
                setNumbersStyle(i);
                i++;
            }
        }
        Arrays.sort(setNumListOrdered);
    }

    public void showImage(boolean isCorrect) {

        // ダイアログの背景を完全に透過。
        settingsDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.my_dialog
                , null));
        ImageView imageView = (ImageView) settingsDialog.findViewById(R.id.dialogIcon);
        if (isCorrect) {
            imageView.setImageResource(R.drawable.correct);
        } else {
            imageView.setImageResource(R.drawable.incorrect);
        }
        settingsDialog.show();
    }

    private void resetItems() {
        btnList = new ArrayList<>();
        setNumList = new int[stageNum];
        setNumListOrdered = new int[stageNum];
        countClicked = 0;
        if (settingsDialog != null) {
            settingsDialog.dismiss();
        }
    }

    private void setNumbersStyle(int i) {
        btnList.get(i).setBackground(null);
        btnList.get(i).setWidth(80);
        btnList.get(i).setHeight(80);
        btnList.get(i).setTextSize(50);
    }
}
