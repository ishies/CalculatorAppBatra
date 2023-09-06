package com.example.calculatorappbatra;


import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ButtonPage extends AppCompatActivity {

    String evaluate = "";
    public void addToCalc(String n) {
        evaluate += n;

        Button b = (Button) this.findViewById(R.id.answer);
        b.setText(evaluate);
    }

    public void evaluate () {
        // int answer =
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_page);
    }
}