package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Buy extends AppCompatActivity implements View.OnClickListener {

    public static EditText enterCity, enterSurveyNo;
    public static Button getCity, getSurveyNo, viewResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        enterCity = findViewById(R.id.enterCity);
        enterSurveyNo = findViewById(R.id.enterSurveyNo);
        getCity = findViewById(R.id.search_city);
        getSurveyNo = findViewById(R.id.search_survey);
        viewResponse = findViewById(R.id.viewResponse);

        getCity.setOnClickListener(this);
        getSurveyNo.setOnClickListener(this);
        viewResponse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_city:
                searchCity();
                break;

            case R.id.search_survey:
                searchSurvey();
                break;

            case R.id.viewResponse:
                responseView();
                break;
        }
    }

    private void searchCity() {
        String city = enterCity.getText().toString();
        if (city.equals("") || city.length()==0) {
            Toast.makeText(this, "Fill Out City!", Toast.LENGTH_LONG).show();
        } else {
            Intent i = new Intent(this, searchCity.class);
            i.putExtra("City",city);
            startActivity(i);
        }
    }

    private void searchSurvey() {
        String survey = enterSurveyNo.getText().toString();
        if (survey.equals("") || survey.length()==0) {
            Toast.makeText(this,"Fill Out Survey Number!", Toast.LENGTH_LONG).show();
        } else {
            Intent i = new Intent(this, searchSurvey.class);
            i.putExtra("SurveyNo",survey);
            startActivity(i);
        }
    }

    private void responseView() {
        Intent i = new Intent(this, com.example.jllusers.viewResponse.class);
        startActivity(i);
    }
}
