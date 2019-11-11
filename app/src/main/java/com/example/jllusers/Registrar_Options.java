package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Registrar_Options extends AppCompatActivity {

    private static String aadhar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar__options);
        Intent i = getIntent();
        aadhar = i.getStringExtra("Aadhar");
//        Toast.makeText(this, "Login Successful!",Toast.LENGTH_SHORT).show();
    }

    public void AssetCreation (View view) {
        Intent i = new Intent(this, AssetCreation.class);
        i.putExtra("Aadhar",aadhar);
        startActivity(i);
    }

    public void AssetModification (View view) {
        Intent i = new Intent(this, AssetModification.class);
        i.putExtra("Aadhar",aadhar);
        startActivity(i);
    }

    public void Registration (View view) {
        Intent i = new Intent(this, AssetRegistration.class);
        i.putExtra("Aadhar",aadhar);
        startActivity(i);
        finish();
    }

}
