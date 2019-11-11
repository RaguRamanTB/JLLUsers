package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Selling_Items extends AppCompatActivity implements View.OnClickListener{

    public static TextView sur,doc,pat,dim,gVal,mVal,lType,aType,own,loc, forsale;

    public static Button regForSale, revertSale;

    public static String survey_no;

    static String getDNo="", getPNo="", getDim="", getGValue="", getMValue="", getOwner="", id="", getALoc="", getForSale="", getLType="",getAType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling__items);
        Intent i = getIntent();
        survey_no = i.getStringExtra("survey_no");
        init();
        setListeners();
        sur.setText(survey_no);
        getJSON(survey_no);
    }

    private void setListeners() {
        regForSale.setOnClickListener(this);
        revertSale.setOnClickListener(this);
    }

    private void getJSON(String survey) {
        final String sur_no = survey;
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
                String BASE_URL = "https://7f45ac9d.ngrok.io/api/Land/";
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
//                    JSONArray jsonArray = new JSONArray(myResponse);
                    JSONObject jsonObject = new JSONObject(myResponse);
//                    for(int i=0; i<jsonArray.length();i++) {
//                        jsonObject = jsonArray.getJSONObject(i);
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

//                    }
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

    private void init() {
        sur = findViewById(R.id.sur);
        doc = findViewById(R.id.doc);
        pat = findViewById(R.id.pat);
        dim = findViewById(R.id.dim);
        gVal = findViewById(R.id.gVal);
        mVal = findViewById(R.id.mVal);
        lType = findViewById(R.id.lType);
        aType = findViewById(R.id.aType);
        own = findViewById(R.id.own);
        loc = findViewById(R.id.loc);
        forsale = findViewById(R.id.forsale);

        regForSale = findViewById(R.id.regForSale);
        revertSale = findViewById(R.id.revertSale);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regForSale:
                putForSale();
                break;

            case R.id.revertSale:
                putRevertSale();
                break;
        }
    }

    private void putRevertSale() {
        String getSNo = sur.getText().toString();
        String getDNo = doc.getText().toString();
        String getPNo = pat.getText().toString();
        String getDim = dim.getText().toString();
        String getGValue = gVal.getText().toString();
        String getMValue = mVal.getText().toString();
        String getLType = lType.getText().toString();
        String getAType = aType.getText().toString();
        String getOwner = own.getText().toString();
        String getALoc = loc.getText().toString();

        String postOwner = "org.jll.hack.User#"+getOwner;

        final JSONObject upload = new JSONObject();
        try {
            upload.put("$class","org.jll.hack.Land");
            upload.put("survey_no",getSNo);
            upload.put("document_no",getDNo);
            upload.put("patta_no",getPNo);
            upload.put("dimension",getDim);
            upload.put("guideline_value",getGValue);
            upload.put("market_value",getMValue);
            upload.put("land_type",getLType);
            upload.put("approval_type",getAType);
            upload.put("owner",postOwner);
            upload.put("location",getALoc);
            upload.put("forsale","no");
            String JSON = upload.toString();
            Log.e("TAG", JSON);
            sendDataToServer2(JSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendDataToServer2(String json) {
        final String JSON = json;
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                return getServerResponse(JSON);
            }

            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(Selling_Items.this,"Land Reverted from Registration",Toast.LENGTH_LONG).show();
                forsale.setText("no");
            }
        }.execute();
    }

    private void putForSale() {
        String getSNo = sur.getText().toString();
        String getOwner = own.getText().toString();

        String postOwner = "org.jll.hack.User#"+getOwner;
        String postLand = "org.jll.hack.Land#"+getSNo;

        final JSONObject upload = new JSONObject();
        try {
            upload.put("$class","org.jll.hack.Register_for_sale");
            upload.put("seller",postOwner);
            upload.put("land",postLand);
            String JSON = upload.toString();
            Log.e("TAG", JSON);
            sendDataToServer(JSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendDataToServer(String json) {
        final String JSON = json;
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                return getServerResponse2(JSON);
            }

            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(Selling_Items.this,"Land Set For Registration",Toast.LENGTH_LONG).show();
                forsale.setText("YES");
            }
        }.execute();
    }

    private String getServerResponse(String json) {
        String surveyNo = sur.getText().toString();
        final String BASE_URL = "https://7f45ac9d.ngrok.io/api/Land/";
        final String finalURL = BASE_URL+surveyNo;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(finalURL)
                .put(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
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

    private String getServerResponse2(String json) {
        final String BASE_URL = "https://7f45ac9d.ngrok.io/api/Register_for_sale";
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
}
