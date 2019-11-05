package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToSeller (View view) {
        Intent intent = new Intent(this, User.class);
        startActivity(intent);
    }

    public void goToBuyer (View view) {
        Intent in = new Intent(this, Registrar.class);
        startActivity(in);
    }
}
