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

public class Sell extends AppCompatActivity {

    private static ListView listView;
    private static ArrayAdapter arrayAdapter;
    private static ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        listView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter(this,R.layout.list_view,arrayList);
    }

    public void ViewAssets(View view) {
        final String urlFinal = "https://7f45ac9d.ngrok.io/api/Land";
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
//                    String data = "";
                    for(int i=0; i<jsonArray.length();i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        String survey_no = jsonObject.optString("survey_no");
//                        String document_no = jsonObject.optString("document_no");
//                        String patta_no = jsonObject.optString("patta_no");
                        arrayList.add(survey_no);
//                        arrayList.add(document_no);
//                        data += "\nSurvey No  :  "+survey_no+"\nDocument_ID  :  "+document_no+"\npatta No  :  "+patta_no+"\n\n";
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
}
