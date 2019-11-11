package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.app.AlertDialog;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class viewResponse extends AppCompatActivity {
    private static ListView listView;
    private static ArrayAdapter arrayAdapter;
    private static ArrayList<String> arrayList = new ArrayList<>();
    private static ArrayList<String> notificationList = new ArrayList<>();
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_response);
        listView = (ListView) findViewById(R.id.responseList);
        arrayAdapter = new ArrayAdapter(this,R.layout.list_view,arrayList);

        final String urlFinal = "http://715863b8.ngrok.io/list_notify.php";
        AsyncTask asyncTask = new AsyncTask() {

            @Override
            protected void onPreExecute() {
                alertDialog = new AlertDialog.Builder(viewResponse.this).create();
                alertDialog.setTitle("Request Status");
            }

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
                        String survey_no = jsonObject.optString("survey");
                        String notification = jsonObject.optString("notify");
                        arrayList.add(survey_no);
                        notificationList.add(notification);
                    }

                    listView.setAdapter(arrayAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String notify = notificationList.get(position);
                            String notMessage = "";
                            if (notify.equals("0")) {
                                notMessage = "Request CANCELLED by seller!";
                            } else if (notify.equals("1")) {
                                notMessage = "Your Request is ON HOLD!";
                            } else if (notify.equals("2")) {
                                notMessage = "Request APPROVED by seller!";
                            }
                            alertDialog.setMessage(notMessage);
                            alertDialog.show();
//                            Toast.makeText(getApplicationContext(),notify,Toast.LENGTH_LONG).show();

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }
}
