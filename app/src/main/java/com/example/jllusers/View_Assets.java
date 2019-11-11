package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class View_Assets extends AppCompatActivity {
    private static ListView listView;
    private static ArrayAdapter arrayAdapter;
    private static ArrayList<String> arrayList = new ArrayList<>();
    private static ArrayList<String> notificationList = new ArrayList<>();
    AlertDialog alert;

    AlertDialog.Builder builder;

    private static String aadhar, notify, response, type;

    public static String buyerID, sellerID, survey_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__assets);
        Intent i = getIntent();
        aadhar = i.getStringExtra("Aadhar");
        type = "UpdateNotification";
        listView = (ListView) findViewById(R.id.requestList);
        arrayAdapter = new ArrayAdapter(this,R.layout.list_view,arrayList);
        final String urlFinal = "http://715863b8.ngrok.io/list_notify.php";
        final BackgroundNotification backgroundNotification = new BackgroundNotification(getApplicationContext());
        AsyncTask asyncTask = new AsyncTask() {

            @Override
            protected void onPreExecute() {
                builder = new AlertDialog.Builder(View_Assets.this);
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
                    arrayList.clear();
                    notificationList.clear();
                    for(int i=0; i<jsonArray.length();i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        buyerID = jsonObject.optString("buyer");
                        sellerID = jsonObject.optString("seller");
                        survey_no = jsonObject.optString("survey");
                        String notification = jsonObject.optString("notify");
                        if (sellerID.equals(aadhar) && notification.equals("1")) {
                            arrayList.add(survey_no);
                            notificationList.add(buyerID);
                        }
                    }

                    listView.setAdapter(arrayAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            notify = notificationList.get(position);
                            String notMessage = "Do you want to approve the request for Buyer ID : "+notify+" ?";
                            builder.setMessage(notMessage)
                                    .setCancelable(false)
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            response = "2";
//                                            Toast.makeText(getApplicationContext(),"YES",Toast.LENGTH_LONG).show();
                                            backgroundNotification.execute(type,survey_no,notify,response);
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            response = "0";
//                                            Toast.makeText(getApplicationContext(),"NO",Toast.LENGTH_LONG).show();
                                            backgroundNotification.execute(type,survey_no,notify,response);
                                        }
                                    });
                            alert = builder.create();
                            alert.setTitle("Your Response");
                            alert.show();
//                            backgroundNotification.execute(type,survey_no,notify,response);
//                            Toast.makeText(getApplicationContext(),notify+response,Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();
//        Toast.makeText(this,notify+response,Toast.LENGTH_LONG).show();
    }
}
