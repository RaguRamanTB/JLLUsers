package com.example.jllusers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp_Fragment_b extends Fragment implements OnClickListener {
    private static View view;
    private static EditText fullName, emailId, mobileNumber, location,
            password, confirmPassword, identity;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;

    public SignUp_Fragment_b() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_fragment_b, container, false);
        initViews();
        setListeners();
        return view;
    }

    private void initViews() {
        fullName = (EditText) view.findViewById(R.id.fullName_b);
        emailId = (EditText) view.findViewById(R.id.userEmailId_b);
        mobileNumber = (EditText) view.findViewById(R.id.mobileNumber_b);
        location = (EditText) view.findViewById(R.id.location_b);
        identity = (EditText) view.findViewById(R.id.identity_b);
        password = (EditText) view.findViewById(R.id.password_b);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPassword_b);
        signUpButton = (Button) view.findViewById(R.id.signUpBtn_b);
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
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn_b:
                checkValidation();
                break;

            case R.id.already_user_b:
                new Buyer().replaceLoginFragment();
                break;
        }

    }

    private void checkValidation() {

        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getLocation = location.getText().toString();
        String getPassword = password.getText().toString();
        String getIdentity = identity.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getIdentity.length() == 0 || getIdentity.equals("")
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

        else
            Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
                    .show();

    }
}
