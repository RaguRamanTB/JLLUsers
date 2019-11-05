package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class User_Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__options);
        Toast.makeText(this, "Login Successful!",Toast.LENGTH_SHORT).show();
    }

    public void goToBuy (View view) {
        Intent intent = new Intent(this, Buy.class);
        startActivity(intent);
    }

    public void goToSell (View view) {
        Intent intent = new Intent(this, Sell.class);
        startActivity(intent);
    }

    public void goToView (View view) {
        Intent intent = new Intent(this, View_Assets.class);
        startActivity(intent);
    }
}
