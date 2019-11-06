package com.example.jllusers;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class SignUp_Fragment_b extends Fragment implements OnClickListener {

    private static final String TAG = "SignUp_Fragment";
    private TextView location, mdob;
    private DatePickerDialog.OnDateSetListener mdate;
    private static View view;
    private static EditText fullName, emailId, mobileNumber, password, confirmPassword, identity;
    private static TextView login;
    private static Button signUpButton, locateButton;
    private static CheckBox terms_conditions;

    public SignUp_Fragment_b() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_fragment_b, container, false);

        String apikey = "AIzaSyA-jTNigOJ8f3zQ6qketJ1QRVLTy7rkduo";

        mdob = (TextView) view.findViewById(R.id.dob);
        mdob.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mdate,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG,"onSetDate: Date(dd/mm/yyyy): "+dayOfMonth+"/"+month+"/"+year);
                String date = dayOfMonth+"/"+month+"/"+year;
                mdob.setText(date);
            }
        };

        if(!Places.isInitialized()) {
            Places.initialize(getContext(),apikey);
        }

        initViews();
        setListeners();
        return view;
    }

    private void initViews() {
        fullName = (EditText) view.findViewById(R.id.fullName_b);
        emailId = (EditText) view.findViewById(R.id.userEmailId_b);
        mobileNumber = (EditText) view.findViewById(R.id.mobileNumber_b);
        location = (TextView) view.findViewById(R.id.location_b);
        identity = (EditText) view.findViewById(R.id.identity_b);
        password = (EditText) view.findViewById(R.id.password_b);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPassword_b);
        signUpButton = (Button) view.findViewById(R.id.signUpBtn_b);
        locateButton = (Button) view.findViewById(R.id.located_b);
        login = (TextView) view.findViewById(R.id.already_user_b);
        terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions_b);

        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    private void setListeners() {
        locateButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    public void searchLoc() {
        int AUTOCOMPLETE_REQUEST_CODE = 1;
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypeFilter(TypeFilter.CITIES)
                .build(getContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                location.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.located_b:
                searchLoc();
                break;

            case R.id.signUpBtn_b:
                checkValidation();
                break;

            case R.id.already_user_b:
                new Registrar().replaceLoginFragment();
                break;
        }

    }

    private void checkValidation() {

        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getDob = mdob.getText().toString();
        String getLocation = location.getText().toString();
        String getPassword = password.getText().toString();
        String getIdentity = identity.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        String type = "Register";

        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getIdentity.length() == 0
                || getDob.length() == 0
                || getConfirmPassword.length() == 0)

            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");

        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");

        else if (!getConfirmPassword.equals(getPassword))
            new CustomToast().Show_Toast(getActivity(), view,
                    "Both password doesn't match.");

        else if (!terms_conditions.isChecked())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Please select Terms and Conditions.");

        else if (getIdentity.length() != 12)
            new CustomToast().Show_Toast(getActivity(), view,
                    "Please enter Valid Aadhar Number.");
        else {
//            Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
//                    .show();
            BackgroundWorker_b backgroundWorker = new BackgroundWorker_b(getContext());
            backgroundWorker.execute(type, getFullName, getEmailId, getMobileNumber, getDob, getLocation, getIdentity, getPassword);
        }
    }
}
