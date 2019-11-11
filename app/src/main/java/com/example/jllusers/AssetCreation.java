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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AssetCreation extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    public static String[] LandType = {"Commercial Land","Agricultural Land","Residential Land","Industrial Land"};
    public static String[] ApprovalType = {"DTCP","Panchayat","CDMA"};
    public static String lType = "";
    public static String aType = "";

    public static EditText SurNo,DocNo, PatNo, Dimension, gValue, mValue, owner;
    public static TextView aLoc;
    public static Button createAsset, assetLoc;

    private static String JSON = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_creation);

        String apikey = "AIzaSyA-jTNigOJ8f3zQ6qketJ1QRVLTy7rkduo";

        Spinner spin = (Spinner) findViewById(R.id.landSpinner);
        ArrayAdapter land = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,LandType);
        land.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(land);
        spin.setOnItemSelectedListener(this);

        Spinner spin2 = (Spinner) findViewById(R.id.approvalSpinner);
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
        SurNo = (EditText) findViewById(R.id.sno);
        DocNo = (EditText) findViewById(R.id.dno);
        PatNo = (EditText) findViewById(R.id.pno);
        Dimension = (EditText) findViewById(R.id.dimension);
        gValue = (EditText) findViewById(R.id.gValue);
        mValue = (EditText) findViewById(R.id.mValue);
        owner = (EditText) findViewById(R.id.owner);
        aLoc = (TextView) findViewById(R.id.aLoc);
        createAsset = (Button) findViewById(R.id.createAsset);
        assetLoc = (Button) findViewById(R.id.assetLoc);
    }

    private void setListeners() {
        assetLoc.setOnClickListener(this);
        createAsset.setOnClickListener(this);
    }

    public void searchAssetLoc() {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner land = (Spinner)parent;
        Spinner approval = (Spinner)parent;

        if(land.getId() == R.id.landSpinner) {
            lType = LandType[position];
        }
        if(approval.getId() == R.id.approvalSpinner) {
            aType = ApprovalType[position];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.assetLoc:
                searchAssetLoc();
                break;

            case R.id.createAsset:
                checkValidation();
                break;

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
            JSON = upload.toString();
            Log.e("TAG",JSON);
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
                Toast.makeText(AssetCreation.this,"Asset created Successfully!",Toast.LENGTH_LONG).show();
            }
        }.execute();
    }

    private String getServerResponse(String json) {
        final String BASE_URL = "https://7f45ac9d.ngrok.io/api/Land";
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
