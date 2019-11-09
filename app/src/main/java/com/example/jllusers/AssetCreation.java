package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

        }
    }
}
