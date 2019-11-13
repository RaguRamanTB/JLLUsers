package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AssetModification extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    public static String[] LandType = {"Commercial Land","Agricultural Land","Residential Land","Industrial Land"};
    public static String[] ApprovalType = {"DTCP","Panchayat","CDMA"};
    public static String lType = "";
    public static String aType = "";

    public static EditText SurNo, Dimension, gValue, mValue;
    public static TextView aLoc,DocNo, PatNo, owner;
    public static Button modifyAsset, assetLoc, searchSurveyNo;

    public static String surveyNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_modification);

        String apikey = "AIzaSyA-jTNigOJ8f3zQ6qketJ1QRVLTy7rkduo";

        Spinner spin = (Spinner) findViewById(R.id.landSpinner_m);
        ArrayAdapter land = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,LandType);
        land.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(land);
        spin.setOnItemSelectedListener(this);

        Spinner spin2 = (Spinner) findViewById(R.id.approvalSpinner_m);
        ArrayAdapter approval = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,ApprovalType);
        approval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(approval);
        spin2.setOnItemSelectedListener(this);

        if(!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),apikey);
        }
        initViews();
        setListeners();
    }

    private void initViews() {
        SurNo = (EditText) findViewById(R.id.sno_m);
        DocNo = (TextView) findViewById(R.id.dno_m);
        PatNo = (TextView) findViewById(R.id.pno_m);
        Dimension = (EditText) findViewById(R.id.dimension_m);
        gValue = (EditText) findViewById(R.id.gValue_m);
        mValue = (EditText) findViewById(R.id.mValue_m);
        owner = (TextView) findViewById(R.id.owner_m);
        aLoc = (TextView) findViewById(R.id.aLoc_m);
        modifyAsset = (Button) findViewById(R.id.modifyAsset);
        assetLoc = (Button) findViewById(R.id.assetLoc_m);
        searchSurveyNo = (Button) findViewById(R.id.searchSurveyNo);
    }

    private void setListeners() {
        assetLoc.setOnClickListener(this);
        modifyAsset.setOnClickListener(this);
        searchSurveyNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.assetLoc_m:
                searchAssetLoc2();
                break;

            case R.id.modifyAsset:
                checkValidation();
                break;

            case R.id.searchSurveyNo:
                searchAssets();
                break;
        }
    }

    private void searchAssets() {
        surveyNo = SurNo.getText().toString();
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
//                String BASE_URL = "https://db9a43d6.ngrok.io/api/Land?filter=%7B\"where\"%3A%7B\"survey_no\"%3A\"";
//                String END_URL = "\"%7D%7D";
//                String urlFinal = BASE_URL+surveyNo+END_URL;
                String BASE_URL = "https://db9a43d6.ngrok.io/api/Land/";
                String urlFinal = BASE_URL+surveyNo;
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
                    String getDNo="", getPNo="", getDim="", getGValue="", getMValue="", getOwner="", id="", getALoc="";
//                    for(int i=0; i<jsonArray.length();i++) {
//                        jsonObject = jsonArray.getJSONObject(i);
                        getDNo = jsonObject.optString("document_no");
                        getPNo = jsonObject.optString("patta_no");
                        getDim = jsonObject.optString("dimension");
                        getGValue = jsonObject.optString("guideline_value");
                        getMValue = jsonObject.optString("market_value");
                        getOwner = jsonObject.optString("owner");
                        id = getOwner.substring(getOwner.length() - 12);
                        getALoc = jsonObject.optString("location");

//                    }
                    DocNo.setText(getDNo);
                    PatNo.setText(getPNo);
                    Dimension.setText(getDim);
                    gValue.setText(getGValue);
                    mValue.setText(getMValue);
                    owner.setText(id);
                    aLoc.setText(getALoc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    private void putJSON() {
        String getSNo = SurNo.getText().toString();
        String getDNo = DocNo.getText().toString();
        String getPNo = PatNo.getText().toString();
        String getDim = Dimension.getText().toString();
        String getGValue = gValue.getText().toString();
        String getMValue = mValue.getText().toString();
        String getOwner = owner.getText().toString();
        String getALoc = aLoc.getText().toString();

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
            upload.put("land_type",lType);
            upload.put("approval_type",aType);
            upload.put("owner",postOwner);
            upload.put("location",getALoc);
            upload.put("forsale","no");
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
                return getServerResponse(JSON);
            }

            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(AssetModification.this,"Asset Modified Successfully!",Toast.LENGTH_LONG).show();
            }
        }.execute();
    }

    private String getServerResponse(String json) {
        surveyNo = SurNo.getText().toString();
        final String BASE_URL = "https://db9a43d6.ngrok.io/api/Land/";
        final String finalURL = BASE_URL+surveyNo;
//        Toast.makeText(this,finalURL,Toast.LENGTH_LONG).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner land = (Spinner)parent;
        Spinner approval = (Spinner)parent;

        if(land.getId() == R.id.landSpinner_m) {
            lType = LandType[position];
        }
        if(approval.getId() == R.id.approvalSpinner_m) {
            aType = ApprovalType[position];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void searchAssetLoc2() {
        int AUTOCOMPLETE_REQUEST_CODE = 1;
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.CITIES)
                .build(getApplicationContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                aLoc.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
//                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void checkValidation () {
        String getSNo = SurNo.getText().toString();
        String getDNo = DocNo.getText().toString();
        String getPNo = PatNo.getText().toString();
        String getDim = Dimension.getText().toString();
        String getGValue = gValue.getText().toString();
        String getMValue = mValue.getText().toString();
        String getOwner = owner.getText().toString();
        String getALoc = aLoc.getText().toString();

        if (getSNo.equals("") || getSNo.length()==0
                || getDNo.equals("") || getDNo.length()==0
                || getPNo.equals("") || getPNo.length()==0
                || getDim.equals("") || getDim.length()==0
                || getGValue.equals("") || getGValue.length()==0
                || getMValue.equals("") || getMValue.length()==0
                || getOwner.equals("") || getOwner.length()==0
                || getALoc.length()==0 ) {
            Toast.makeText(this,"All fields are required! Fill everything.",Toast.LENGTH_LONG).show();
        }
        else {
            putJSON();
        }
    }

}
