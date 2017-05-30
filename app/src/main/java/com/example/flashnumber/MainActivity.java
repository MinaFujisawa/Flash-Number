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
    private static final int MAX_STAGE = 10;
    private static final int MAX_INCORRECT = 2;
    private static final int MAX_NUM = 10;

    private static final String KEY_SCORE = "flashnumber_score";
    private int stageNum = MIN_STAGE;
    private TextView mCountDownTextView;
    private TextView mDescriptionTextView;
    private int[] setNumList;
    private int[] setNumListOrdered;
    private ArrayList<Button> btnList;
    private int countClicked;
    private int score;
    private int countIncorrect;
    Dialog settingsDialog;


    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        int i = 0;

        //get btn num index
        int btnIndex = getButtonIndex(b);
        btnList.get(btnIndex).setEnabled(false);
        setNumbersStyle(btnIndex);
        //check the answers
        if (countClicked + 1 != stageNum) {
            //correct
            if (setNumList[btnIndex] == (setNumListOrdered[countClicked])) {
                b.setText(String.valueOf(setNumListOrdered[countClicked]));
                countClicked++;
                //incorrect
            } else {
                showImage(false);
                countIncorrect++;

                // display the rest of the answers
                for (int j = 0; j < stageNum; j++) {
                    if (((String) btnList.get(j).getText()).equals("")) {
                        btnList.get(j).setText(String.valueOf(setNumList[j]));
                        btnList.get(j).setTextColor(getResources().getColor(R.color.incorrect));
                    }
                }

                // go to the result page
                if (countIncorrect >= MAX_INCORRECT) {
                    new CountDownTimer(2000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            Intent intent = new Intent(getApplication(), ResultActivity.class);
                            intent.putExtra(KEY_SCORE, score);
                            startActivity(intent);
                        }
                    }.start();
                    // back to the previous stage
                } else {
                    if (stageNum != MIN_STAGE) {
                        stageNum--;
                    }
                    new CountDownTimer(1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            loadActivity();
                        }
                    }.start();
                }
            }
            // all correct
        } else {
            showImage(true);
            b.setText(String.valueOf(setNumListOrdered[countClicked]));
            addScore(stageNum);
            if (stageNum != MAX_STAGE) {
                stageNum++;
            }

            new CountDownTimer(1000, 1000) {
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
            case 6:
                setContentView(R.layout.num_six);
                resetItems();
                break;
            case 7:
                setContentView(R.layout.num_seven);
                resetItems();
                break;
            case 8:
                setContentView(R.layout.num_eight);
                resetItems();
                break;
            case 9:
                setContentView(R.layout.num_nine);
                resetItems();
                break;
            case 10:
                setContentView(R.layout.num_ten);
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
        btnList.add((Button) findViewById(R.id.btn6));
        btnList.add((Button) findViewById(R.id.btn7));
        btnList.add((Button) findViewById(R.id.btn8));
        btnList.add((Button) findViewById(R.id.btn9));
        btnList.add((Button) findViewById(R.id.btn10));


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
                //visible number
                new CountDownTimer(2000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    //invisible number
                    public void onFinish() {
                        for (int i = 0; i < stageNum; i++) {
                            setButtonStyle(i);
                            mDescriptionTextView.setText(R.string.description2);

                        }
                    }
                }.start();
            }
        }.start();
    }

    public void setNum(int clickNum) {
        boolean num[] = new boolean[MAX_NUM + 1];
        int i = 0;
        while (i < clickNum) {
            int p = random.nextInt(MAX_NUM) + 1;
            if (num[p] == false) {
                btnList.get(i).setText(String.valueOf(p));
                setNumListOrdered[i] = p;
                setNumList[i] = p;
                num[p] = true;
                setNumbersStyle(i);
                i++;
            }
        }
        Arrays.sort(setNumListOrdered);
    }

    public void showImage(boolean isCorrect) {

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
//        btnList.get(i).setWidth(80);
//        btnList.get(i).setHeight(80);
//        btnList.get(i).setTextSize(50);
    }

    private void setButtonStyle(int i) {
        btnList.get(i).setText("");
        btnList.get(i).setEnabled(true);
        btnList.get(i).setBackgroundResource(R.drawable.clicable_buttton_bg);
        btnList.get(i).setWidth(55);
        btnList.get(i).setHeight(55);
        btnList.get(i).setTextSize(45);
    }

    private void addScore(int stageNum) {
        switch (stageNum) {
            case 4:
                score += 1;
                break;
            case 5:
                score += 2;
                break;
            case 6:
                score += 4;
                break;
            case 7:
                score += 8;
                break;
            case 8:
                score += 16;
                break;
            case 9:
                score += 32;
                break;
            case 10:
                score += 64;
                break;
        }
    }

    private int getButtonIndex(Button b) {
        int length = String.valueOf(b).length();
        String substring = String.valueOf(b).substring(length - 3, length);
        return Integer.parseInt(substring.replaceAll("[^0-9]", "")) - 1;
    }
}
