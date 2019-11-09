package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class searchCity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        TextView city = (TextView) findViewById(R.id.city);
        Intent i = getIntent();
        String getCity = i.getStringExtra("City");
        city.setText(getCity);
    }
}
