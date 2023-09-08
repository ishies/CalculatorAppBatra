package com.example.calculatorappbatra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public void buttonPage (View v) {
        Intent intent = new Intent(this, ButtonPage.class);
        startActivity(intent);
    }

    public void typedPage (View v) {
        Intent intent = new Intent(this, TypedPage.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}