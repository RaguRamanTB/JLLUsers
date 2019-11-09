package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Selling_Items extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling__items);
        TextView textView = (TextView) findViewById(R.id.survey_no);

        Intent i = getIntent();

        String survey_no = i.getStringExtra("survey_no");

        textView.setText(survey_no);
    }
}
