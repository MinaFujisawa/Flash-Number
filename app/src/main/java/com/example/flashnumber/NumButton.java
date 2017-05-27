package com.example.flashnumber;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by MinaFujisawa on 2017/05/27.
 */

public class NumButton extends MainActivity{
    private static final int MAX_NUM = 15;
    private ArrayList<Button> btnList = new ArrayList<>();

    public NumButton(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layinfView = inflater.inflate(R.layout.num_four, null);

        btnList.add((Button) layinfView.findViewById(R.id.btn1));
        btnList.add((Button) layinfView.findViewById(R.id.btn2));
        btnList.add((Button) layinfView.findViewById(R.id.btn3));
        btnList.add((Button) layinfView.findViewById(R.id.btn4));
    }

    public ArrayList<Button> getBtnList(int clickNum) {
        return btnList;
    }
}
