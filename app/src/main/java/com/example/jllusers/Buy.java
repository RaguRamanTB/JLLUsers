package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Buy extends AppCompatActivity implements View.OnClickListener {

    public static EditText enterCity, enterSurveyNo;
    public static Button getCity, getSurveyNo, viewResponse;
    private static String aadhar;
    static String isFS = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        Intent i = getIntent();
        aadhar = i.getStringExtra("Aadhar");

        enterCity = findViewById(R.id.enterCity);
        enterSurveyNo = findViewById(R.id.enterSurveyNo);
        getCity = findViewById(R.id.search_city);
        getSurveyNo = findViewById(R.id.search_survey);
        viewResponse = findViewById(R.id.viewResponse);

        getCity.setOnClickListener(this);
        getSurveyNo.setOnClickListener(this);
        viewResponse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_city:
                searchCity();
                break;

            case R.id.search_survey:
                searchSurvey();
                break;

            case R.id.viewResponse:
                responseView();
                break;
        }
    }

    private void searchCity() {
        String city = enterCity.getText().toString();
        if (city.equals("") || city.length()==0) {
            Toast.makeText(this, "Fill Out City!", Toast.LENGTH_LONG).show();
        } else {
            Intent i = new Intent(this, searchCity.class);
            i.putExtra("Aadhar",aadhar);
            i.putExtra("City",city);
            startActivity(i);
        }
    }

    private void searchSurvey() {
        String survey = enterSurveyNo.getText().toString();
        if (survey.equals("") || survey.length()==0) {
            Toast.makeText(this,"Fill Out Survey Number!", Toast.LENGTH_LONG).show();
        } else {
            jumpToBuyingItems();
        }
    }

    private void jumpToBuyingItems() {
        final String survey = enterSurveyNo.getText().toString();
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
                String BASE_URL = "https://7f45ac9d.ngrok.io/api/Land/";
                String urlFinal = BASE_URL+survey;
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
                    String getForSale = jsonObject.optString("forsale");
                    String getUser = jsonObject.optString("owner");
                    String id = getUser.substring(getUser.length()-12);
                    if ((getForSale.equals("YES") || getForSale.equals("yes")) && !id.equals(aadhar)) {
                        Intent i = new Intent(Buy.this, Buying_Item.class);
                        i.putExtra("Aadhar",aadhar);
                        i.putExtra("survey_no",survey);
                        startActivity(i);
                    } else if (id.equals(aadhar)) {
                        Toast.makeText(getApplicationContext(),"You have entered your own land!",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Land Not for Sale!",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    private void responseView() {
        Intent i = new Intent(this, com.example.jllusers.viewResponse.class);
        i.putExtra("Aadhar",aadhar);
        startActivity(i);
    }
}
