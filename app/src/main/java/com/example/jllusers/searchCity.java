package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class searchCity extends AppCompatActivity {
    private static ListView listView;
    private static ArrayAdapter arrayAdapter;
    private static ArrayList<String> arrayList = new ArrayList<>();
    public static String getCity,aadhar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        Intent i = getIntent();
        getCity = i.getStringExtra("City");
        aadhar = i.getStringExtra("Aadhar");

        listView = (ListView) findViewById(R.id.cityList);

        arrayAdapter = new ArrayAdapter(this,R.layout.list_view,arrayList);

        cityList();
    }

    private void cityList() {
        String BASE_URL = "https://db9a43d6.ngrok.io/api/Land?filter=%7B\"where\"%3A%7B\"location\"%3A\"";
        String END_URL = "\"%7D%7D";
        final String urlFinal = BASE_URL+getCity+END_URL;
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
                String forsale="";
                String myResponse = o.toString();
                try {
                    JSONArray jsonArray = new JSONArray(myResponse);
                    JSONObject jsonObject;
                    for(int i=0; i<jsonArray.length();i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String getUser = jsonObject.optString("owner");
                        String id = getUser.substring(getUser.length()-12);
                        String survey_no = jsonObject.optString("survey_no");
                        forsale = jsonObject.optString("forsale");
                        if ((forsale.equals("YES") || forsale.equals("yes")) && !id.equals(aadhar)) {
                            arrayList.add(survey_no);
                        }
                    }

                    listView.setAdapter(arrayAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String survey = arrayList.get(position);

                            Intent i = new Intent(getApplicationContext(),Buying_Item.class);
                            i.putExtra("survey_no",survey);
                            i.putExtra("Aadhar",aadhar);
                            startActivity(i);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }
}
