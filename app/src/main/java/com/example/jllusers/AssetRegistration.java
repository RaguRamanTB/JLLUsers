package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Toast;

public class AssetRegistration extends AppCompatActivity implements View.OnClickListener {

    public static EditText docNo, buyerID, sellerID, mValue;
    public static CheckBox parentDoc, pattaDoc, encumDoc, mapDoc, approvalDoc;
    public static Button register, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_registration);

        initViews();
        setListeners();
    }

    private void initViews() {
        docNo = findViewById(R.id.dno_r);
        buyerID = findViewById(R.id.buyerID);
        sellerID = findViewById(R.id.sellerID);
        mValue = findViewById(R.id.mValue_r);
        parentDoc = findViewById(R.id.parentDoc);
        pattaDoc = findViewById(R.id.pattaDoc);
        encumDoc = findViewById(R.id.encumDoc);
        mapDoc = findViewById(R.id.mapDoc);
        approvalDoc = findViewById(R.id.approvalDoc);
        register = findViewById(R.id.register);
        cancel = findViewById(R.id.cancel);
    }

    private void setListeners() {
        register.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                checkValidation();
                break;

            case R.id.cancel:
                returnBack();
                break;

        }
    }

    private void checkValidation() {
        String getDoc = docNo.getText().toString();
        String getBuyerID = buyerID.getText().toString();
        String getSellerID = sellerID.getText().toString();
        String getMValue = mValue.getText().toString();

        if (getDoc.equals("") || getDoc.length()==0 || getBuyerID.equals("") || getBuyerID.length()==0
            || getSellerID.equals("") || getSellerID.length()==0 || getMValue.equals("") || getMValue.length()==0) {
            Toast.makeText(this,"Fill all fields!",Toast.LENGTH_LONG).show();
        } else if (!parentDoc.isChecked() || !pattaDoc.isChecked() || !encumDoc.isChecked() || !mapDoc.isChecked() || !approvalDoc.isChecked()) {
            Toast.makeText(this,"Documents Missing! Please add all the Documents.",Toast.LENGTH_LONG).show();
        } else {
            registration();
        }
    }

    private void registration() {

    }

    private void returnBack() {
        Intent i = new Intent(this, Registrar_Options.class);
        startActivity(i);
        finish();
    }
}
