package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Buying_Item extends AppCompatActivity implements View.OnClickListener{

    static String getSNOIntent;

    public static TextView sur,doc,pat,dim,gVal,mVal,lType,aType,own,loc, forsale;

    public static Button buyReq;

    static String getAadhar="", getDNo="", getPNo="", getDim="", getGValue="", getMValue="", getOwner="", id="", getALoc="", getForSale="", getLType="",getAType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying__item);
        Intent i = getIntent();
        getSNOIntent = i.getStringExtra("survey_no");
        getAadhar = i.getStringExtra("Aadhar");
        init();
        setListeners();
        getJSON(getSNOIntent);
    }

    private void init() {
        sur = findViewById(R.id.sur_b);
        doc = findViewById(R.id.doc_b);
        pat = findViewById(R.id.pat_b);
        dim = findViewById(R.id.dim_b);
        gVal = findViewById(R.id.gVal_b);
        mVal = findViewById(R.id.mVal_b);
        lType = findViewById(R.id.lType_b);
        aType = findViewById(R.id.aType_b);
        own = findViewById(R.id.own_b);
        loc = findViewById(R.id.loc_b);
        forsale = findViewById(R.id.forsale_b);

        buyReq = findViewById(R.id.buyReq);
    }

    private void setListeners() {
        buyReq.setOnClickListener(this);
    }

    private void getJSON(String json) {
        final String sur_no = json;
        sur.setText(getSNOIntent);
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
                String BASE_URL = "https://db9a43d6.ngrok.io/api/Land/";
                String urlFinal = BASE_URL+sur_no;
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
                    JSONObject jsonObject = new JSONObject(myResponse);
                    getDNo = jsonObject.optString("document_no");
                    getPNo = jsonObject.optString("patta_no");
                    getDim = jsonObject.optString("dimension");
                    getGValue = jsonObject.optString("guideline_value");
                    getMValue = jsonObject.optString("market_value");
                    getLType = jsonObject.optString("land_type");
                    getAType = jsonObject.optString("approval_type");
                    getOwner = jsonObject.optString("owner");
                    id = getOwner.substring(getOwner.length() - 12);
                    getALoc = jsonObject.optString("location");
                    getForSale = jsonObject.optString("forsale");
                    doc.setText(getDNo);
                    pat.setText(getPNo);
                    dim.setText(getDim);
                    gVal.setText(getGValue);
                    mVal.setText(getMValue);
                    lType.setText(getLType);
                    aType.setText(getAType);
                    own.setText(id);
                    loc.setText(getALoc);
                    forsale.setText(getForSale);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buyReq:
                sendNotification();
                break;
        }
    }

    private void sendNotification() {
        String type = "BuyerNotification";
        String getSNO = sur.getText().toString();
        String getDocNo = doc.getText().toString();
        String getSeller = own.getText().toString();
        String notify = "1";
//        BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext());
//        BackgroundNotification backgroundNotification = new BackgroundNotification(getApplicationContext());
//        backgroundNotification.execute(type,getSNO,getDocNo,getAadhar,getSeller,notify);
        BgSendNotification bgSendNotification = new BgSendNotification(getApplicationContext());
        bgSendNotification.execute(type,getSNO,getDocNo,getAadhar,getSeller,notify);

//        Toast.makeText(this,getAadhar,Toast.LENGTH_LONG).show();
    }
}
