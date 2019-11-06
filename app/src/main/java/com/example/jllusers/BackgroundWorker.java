package com.example.jllusers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

public class BackgroundWorker extends AsyncTask <String, Void, String> {

    Context context;
    AlertDialog alertDialog;

    BackgroundWorker (Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... voids) {
        String type = voids[0];
        String login_url = "http://21822f0e.ngrok.io/login.php";
        String register_url = "http://21822f0e.ngrok.io/register.php";
        if (type.equals("Login")) {
            try {
                String getEmailId = voids[1];
                String getPassword = voids[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("getEmailId","UTF-8")+"="+URLEncoder.encode(getEmailId,"UTF-8")+"&"
                        +URLEncoder.encode("getPassword","UTF-8")+"="+URLEncoder.encode(getPassword,"UTF-8");
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
        } else if (type.equals("Register")) {
            try {
                String getFullName = voids[1];
                String getEmailId = voids[2];
                String getMobileNumber = voids[3];
                String getDob = voids[4];
                String getLocation = voids[5];
                String getIdentity = voids[6];
                String getPassword = voids[7];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("getFullName","UTF-8")+"="+URLEncoder.encode(getFullName,"UTF-8")+"&"
                        +URLEncoder.encode("getEmailId","UTF-8")+"="+URLEncoder.encode(getEmailId,"UTF-8")+"&"
                        +URLEncoder.encode("getMobileNumber","UTF-8")+"="+URLEncoder.encode(getMobileNumber,"UTF-8")+"&"
                        +URLEncoder.encode("getDob","UTF-8")+"="+URLEncoder.encode(getDob,"UTF-8")+"&"
                        +URLEncoder.encode("getLocation","UTF-8")+"="+URLEncoder.encode(getLocation,"UTF-8")+"&"
                        +URLEncoder.encode("getIdentity","UTF-8")+"="+URLEncoder.encode(getIdentity,"UTF-8")+"&"
                        +URLEncoder.encode("getPassword","UTF-8")+"="+URLEncoder.encode(getPassword,"UTF-8");
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
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
        if (result.equals("Login Success!")) {
            Intent i = new Intent(context,User_Options.class);
            context.startActivity(i);
            alertDialog.hide();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
