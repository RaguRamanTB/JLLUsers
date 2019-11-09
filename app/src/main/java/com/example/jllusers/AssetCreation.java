package com.example.jllusers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class AssetCreation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static String[] LandType = {"Commercial Land","Agricultural Land","Residential Land","Industrial Land"};
    public static String[] ApprovalType = {"DTCP","Panchayat","CDMA"};
    public static String lType = "";
    public static String aType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_creation);

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

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner land = (Spinner)parent;
        Spinner approval = (Spinner)parent;

        if(land.getId() == R.id.landSpinner)
        {
//            Toast.makeText(this, "Your chose :" + products[position],Toast.LENGTH_SHORT).show();
            lType = LandType[position];
        }
        if(approval.getId() == R.id.approvalSpinner)
        {
//            Toast.makeText(this, "Your chose :" + sellers[position],Toast.LENGTH_SHORT).show();
            aType = ApprovalType[position];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
