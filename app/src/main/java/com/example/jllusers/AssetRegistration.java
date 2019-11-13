package com.example.jllusers;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AssetRegistration extends AppCompatActivity implements View.OnClickListener {

    public static EditText docNo, mValue;
    public static TextView buyerID, sellerID;
    public static CheckBox parentDoc, pattaDoc, encumDoc, mapDoc, approvalDoc;
    public static Button register, cancel, searchDocNo, fillBuyer;

    private static String aadhar;

    public static String sellID, buyID, getMValue="", getOwner="", id="", isForSale="", getPNo, getDim, getGValue, getALoc, lType, aType;

    public static boolean onSale = true;

    public static String documentNo, surveyNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_registration);
        Intent i = getIntent();
        aadhar = i.getStringExtra("Aadhar");
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
        fillBuyer = findViewById(R.id.fillBuyer);
    }

    private void setListeners() {
        register.setOnClickListener(this);
        cancel.setOnClickListener(this);
        searchDocNo.setOnClickListener(this);
        fillBuyer.setOnClickListener(this);
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

            case R.id.fillBuyer:
                getBuyerID();
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
        String bID = buyerID.getText().toString();
        if (!onSale) {
            Toast.makeText(this,"Land not for Sale!",Toast.LENGTH_LONG).show();
        } else if (bID.equals("NO BUYER")) {
            Toast.makeText(this,"No Buyer/Request Not Approved by Seller",Toast.LENGTH_LONG).show();
        } else {
            documentNo = docNo.getText().toString();
            buyID = buyerID.getText().toString();
            sellID = sellerID.getText().toString();
            getMValue = mValue.getText().toString();
            String postLand = "org.jll.hack.Land#"+surveyNo;
            String postBuyer = "org.jll.hack.User#"+buyID;
            String postSeller = "org.jll.hack.User#"+sellID;
            String postRegistrar = "org.jll.hack.Registrar#"+aadhar;

//            Toast.makeText(this,postBuyer,Toast.LENGTH_LONG).show();

            final JSONObject upload = new JSONObject();
            try {
                upload.put("$class","org.jll.hack.Buy");
                upload.put("land",postLand);
                upload.put("buyer",postBuyer);
                upload.put("seller",postSeller);
                upload.put("registrar",postRegistrar);
                upload.put("transaction_amount",getMValue);
                String JSON = upload.toString();
                Log.e("TAG", JSON);
                sendDataToServer(JSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendDataToServer(String json) {
        final String JSON = json;
        final BgSendNotification bgSendNotification = new BgSendNotification(getApplicationContext());
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                return getServerResponse(JSON);
            }

            @Override
            protected void onPostExecute(String s) {
                String getSell = sellerID.getText().toString();
                Toast.makeText(AssetRegistration.this,"Asset Registered Successfully!",Toast.LENGTH_LONG).show();
                bgSendNotification.execute("ClearNotification", getSell);
            }
        }.execute();
    }

    private String getServerResponse(String json) {
        final String BASE_URL = "https://db9a43d6.ngrok.io/api/Buy";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .build();

        Call call = okHttpClient.newCall (request);
        Response response = null;

        try {
            response = call.execute();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unable to contact server!";
    }

    private void registration() {
        documentNo = docNo.getText().toString();
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
                String BASE_URL = "https://db9a43d6.ngrok.io/api/Land?filter=%7B\"where\"%3A%7B\"document_no\"%3A\"";
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
                View view;
                try {
                    JSONArray jsonArray = new JSONArray(myResponse);
                    JSONObject jsonObject;
                    for(int i=0; i<jsonArray.length();i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        surveyNo = jsonObject.optString("survey_no");
                        getPNo = jsonObject.optString("patta_no");
                        getDim = jsonObject.optString("dimension");
                        getGValue = jsonObject.optString("guideline_value");
                        getMValue = jsonObject.optString("market_value");
                        lType = jsonObject.optString("land_type");
                        aType = jsonObject.optString("approval_type");
                        getOwner = jsonObject.optString("owner");
                        id = getOwner.substring(getOwner.length() - 12);
                        getALoc = jsonObject.optString("location");
                        isForSale = jsonObject.optString("forsale");
                    }
                    sellerID.setText(id);
                    mValue.setText(getMValue);

                    if (isForSale.equals("yes") || isForSale.equals("YES") || isForSale.equals("Yes")) {
                        onSale=true;
                    } else {
                        onSale=false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    private void returnBack() {
        Intent i = new Intent(this, Registrar_Options.class);
        startActivity(i);
//        finish();
    }

    private void getBuyerID() {
        String sID = sellerID.getText().toString();
        GetBuyer getBuyer = new GetBuyer(getApplicationContext(), buyerID);
        getBuyer.execute("FillBuy",sID);
    }
}
