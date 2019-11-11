package com.example.jllusers;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AssetRegistration extends AppCompatActivity implements View.OnClickListener {

    public static EditText docNo, buyerID, sellerID, mValue;
    public static CheckBox parentDoc, pattaDoc, encumDoc, mapDoc, approvalDoc;
    public static Button register, cancel, searchDocNo;

    public static String documentNo, surveyNo;
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
        searchDocNo = findViewById(R.id.searchDocNo);
        register = findViewById(R.id.register);
        cancel = findViewById(R.id.cancel);
    }

    private void setListeners() {
        register.setOnClickListener(this);
        cancel.setOnClickListener(this);
        searchDocNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchDocNo:
                registration();
                break;
            
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
        } else if (getSellerID.length()<12 || getBuyerID.length()<12){
            Toast.makeText(this,"Invalid Buyer ID/Seller ID. Check Again!",Toast.LENGTH_LONG).show();
        } else {
            putJSON();
        }
    }

    private void putJSON() {

    }

    private void registration() {
        documentNo = docNo.getText().toString();
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
                String BASE_URL = "https://7f45ac9d.ngrok.io/api/Land?filter=%7B\"where\"%3A%7B\"document_no\"%3A\"";
                String END_URL = "\"%7D%7D";
                String urlFinal = BASE_URL+documentNo+END_URL;
                Request request = new Request.Builder()
                        .url(urlFinal)
                        .build();

                Response response = null;

                try {
                    response = client.newCall(request).execute();
                    return response.body().string();
                }catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                String myResponse = o.toString();
                try {
                    JSONArray jsonArray = new JSONArray(myResponse);
                    JSONObject jsonObject;
                    String getMValue="", getOwner="", id="";
                    for(int i=0; i<jsonArray.length();i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        surveyNo = jsonObject.optString("survey_no");
                        getMValue = jsonObject.optString("market_value");
                        getOwner = jsonObject.optString("owner");
                        id = getOwner.substring(getOwner.length() - 12);
                    }
                    sellerID.setText(id);
                    mValue.setText(getMValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    private void returnBack() {
        Intent i = new Intent(this, Registrar_Options.class);
        startActivity(i);
        finish();
    }
}
