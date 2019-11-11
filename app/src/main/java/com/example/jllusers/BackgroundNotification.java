package com.example.jllusers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundNotification extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;

    BackgroundNotification (Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... voids) {
        String type = voids[0];
        String notification_url = "http://715863b8.ngrok.io/notify_seller.php";
        String update_notification = "http://715863b8.ngrok.io/update_notify.php";
        if (type.equals("BuyerNotification")) {
            try {
                String getSNo = voids[1];
                String getDNo = voids[2];
                String getBuyerID = voids[3];
                String getSellerID = voids[4];
                String getNotification = voids[5];
                URL url = new URL(notification_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("getSNOIntent","UTF-8")+"="+URLEncoder.encode(getSNo,"UTF-8")+"&"
                        +URLEncoder.encode("getDNo","UTF-8")+"="+URLEncoder.encode(getDNo,"UTF-8")+"&"
                        +URLEncoder.encode("getBuyerID","UTF-8")+"="+URLEncoder.encode(getBuyerID,"UTF-8")+"&"
                        +URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(getSellerID,"UTF-8")+"&"
                        +URLEncoder.encode("getNotification","UTF-8")+"="+URLEncoder.encode(getNotification,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine())!=null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("UpdateNotification")) {
            try {
                String getSNo = voids[1];
                String getBuyer = voids[2];
                String notify = voids[3];
                URL url = new URL(update_notification);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("survey_no","UTF-8")+"="+URLEncoder.encode(getSNo,"UTF-8")+"&"
                        +URLEncoder.encode("buyerID","UTF-8")+"="+URLEncoder.encode(getBuyer,"UTF-8")+"&"
                        +URLEncoder.encode("notified","UTF-8")+"="+URLEncoder.encode(notify,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine())!=null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context,result,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}