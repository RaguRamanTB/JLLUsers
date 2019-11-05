package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class User_Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__options);
        Toast.makeText(this, "Login Successful!",Toast.LENGTH_SHORT).show();
    }
}
