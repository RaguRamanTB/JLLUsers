package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class Registrar_Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar__options);
        Toast.makeText(this, "Login Successful!",Toast.LENGTH_SHORT).show();
    }
}
