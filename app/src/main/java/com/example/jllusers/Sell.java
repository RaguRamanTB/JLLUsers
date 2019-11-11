package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearSmoothScroller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Sell extends AppCompatActivity implements View.OnClickListener{

    private static ListView listView;
    private static ArrayAdapter arrayAdapter;

    public static Button viewMyAssets, viewRequests;

    private static String aadhar;

    private static ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        viewMyAssets = findViewById(R.id.viewMyAssets);
        viewRequests = findViewById(R.id.checkRequests);
        setListeners();

        Intent i = getIntent();
        aadhar = i.getStringExtra("Aadhar");

        listView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter(this,R.layout.list_view,arrayList);
    }

    private void setListeners() {
        viewMyAssets.setOnClickListener(this);
        viewRequests.setOnClickListener(this);
    }

    public void ViewAssets() {
        String BASE_URL = "https://7f45ac9d.ngrok.io/api/Land?filter=%7B\"where\"%20%3A%20%7B\"owner\"%3A%20\"resource%3Aorg.jll.hack.User%23";
        String END_URL = "\"%7D%7D";
        final String urlFinal = BASE_URL+aadhar+END_URL;
        arrayList.clear();
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
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
                    for(int i=0; i<jsonArray.length();i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String survey_no = jsonObject.optString("survey_no");
                        arrayList.add(survey_no);
                    }

                    listView.setAdapter(arrayAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String survey = arrayList.get(position);

                            Intent i = new Intent(getApplicationContext(),Selling_Items.class);
                            i.putExtra("survey_no",survey);
                            startActivity(i);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();

    }

    public void CheckRequests () {
        Intent intent = new Intent(this,View_Assets.class);
        intent.putExtra("Aadhar",aadhar);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewMyAssets:
                ViewAssets();
                break;

            case R.id.checkRequests:
                CheckRequests();
                break;

        }
    }
}
