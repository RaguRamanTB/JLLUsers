package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class searchSurvey extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_survey);
        TextView surveyNo = (TextView) findViewById(R.id.survey);
        Intent i = getIntent();
        String getSurvey = i.getStringExtra("SurveyNo");
        surveyNo.setText(getSurvey);
    }
}
