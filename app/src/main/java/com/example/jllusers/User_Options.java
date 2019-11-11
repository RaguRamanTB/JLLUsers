package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class User_Options extends AppCompatActivity {

    private static String aadhar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__options);
        Intent i = getIntent();
        aadhar = i.getStringExtra("Aadhar");
        Toast.makeText(this, "Login Successful!",Toast.LENGTH_LONG).show();
    }

    public void goToBuy (View view) {
        Intent intent = new Intent(this, Buy.class);
        intent.putExtra("Aadhar",aadhar);
        startActivity(intent);
    }

    public void goToSell (View view) {
        Intent intent = new Intent(this, Sell.class);
        intent.putExtra("Aadhar",aadhar);
        startActivity(intent);
    }

}
