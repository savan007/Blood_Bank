package com.sp.bookmarket.views;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sp.bookmarket.R;
import com.sp.bookmarket.models.SignUpResponse;
import com.sp.bookmarket.presenters.SignupListener;
import com.sp.bookmarket.presenters.SignupPresenter;
import com.sp.bookmarket.webservices.ChipsApi;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by savan_007 on 7/5/2016.
 */
public class SignupView extends Activity implements SignupListener {

    @Bind(R.id.first_name)
    EditText firstName;

    @Bind(R.id.last_name)
    EditText lastName;

    @Bind(R.id.user_name)
    EditText userName;

    @Bind(R.id.email_id)
    EditText emailId;

    @Bind(R.id.mobile_number)
    EditText mobileNumber;

    @Bind(R.id.password)
    EditText password;

    @Bind(R.id.Confirm_password)
    EditText confirmPassword;

    @Bind(R.id.birth_date_view)
    TextView dob;

    @Bind(R.id.address)
    EditText address;

    @Bind(R.id.city)
    EditText city;

    @Bind(R.id.state)
    EditText state;

    ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String gender;
    static final int DATE_PICKER_ID = 1111;

    private int year;
    private int month;
    private int day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        final RadioGroup radioGenderGroup=(RadioGroup) findViewById(R.id.radio_gender);

        bookMarketApplication = BookMarketApplication.getApplication(this);
        chipsApi = bookMarketApplication.getChipsApi();
        signupPresenter = new SignupPresenter(this, this, chipsApi);


        //Custom Actionbar Start
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_custom_actionbar, null);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        //View Name
        final TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText(getApplicationContext().getString(R.string.custom_actionbar_title_sign_up));

        //Back Button
        final ImageButton actionBarHomeButton = (ImageButton) findViewById(R.id.action_bar_home_button);
        actionBarHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //right click button
        final ImageButton actionBarActionButton = (ImageButton) findViewById(R.id.action_bar_action_button);
        //actionBarActionButton.setVisibility(View.GONE);

        actionBarActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookMarketApplication.isInternetConnection()) {
                    if (checkNotNull()) {
                        showProgress();
                        int selectedId = radioGenderGroup.getCheckedRadioButtonId();
                        RadioButton radioGenderButton = (RadioButton) findViewById(selectedId);
                        gender = radioGenderButton.getText().toString();

                        signupPresenter.registerUser(firstName.getText().toString(), lastName.getText().toString(), userName.getText().toString(), emailId.getText().toString(), mobileNumber.getText().toString(), password.getText().toString(), dob.getText().toString(), address.getText().toString(), city.getText().toString(), state.getText().toString(), gender);
                    }
                }else {
                    Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.internet_connection_not_available), Toast.LENGTH_LONG).show();
                }
            }
        });
        //Custom Actionbar Complete

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
    }

    private BookMarketApplication bookMarketApplication;
    private SignupPresenter signupPresenter;
    private ChipsApi chipsApi;

    @SuppressWarnings("deprecation")
    @OnClick(R.id.btn_birth_date)
    public void setBirthDate()
    {
        showDialog(DATE_PICKER_ID);
    }

     public boolean checkNotNull()
    {
        boolean checkNotNull = true;

        //check Password and repassword same or not
        if (!checkPasswordAndConfirmPassword(password.getText().toString(), confirmPassword.getText().toString())) {
            confirmPassword.setError("Password and re password does not match");
            checkNotNull = false;
        } else {
            confirmPassword.setError(null);
        }

        return checkNotNull;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            // Show selected date
            dob.setText(new StringBuilder().append(year)
                    .append("/").append(month + 1).append("/").append(day)
                    .append(" "));

        }
    };

    public boolean checkPasswordAndConfirmPassword(String password,String confirmPassword)
    {
        boolean pstatus = false;
        if (confirmPassword != null && password != null)
        {

            if (password.equals(confirmPassword))
            {
                pstatus = true;
            }
        }
        return pstatus;
    }


    private void showProgress() {
        progressDialog = new ProgressDialog(SignupView.this, AlertDialog.THEME_HOLO_LIGHT);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait...!");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    private void dismissProgressDialog() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void validateCredential(String message) {
        dismissProgressDialog();
        //display alert dialog in case of null value of userName/password
        AlertDialog alertDialog = new AlertDialog.Builder(SignupView.this,AlertDialog.THEME_HOLO_LIGHT).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public boolean validateEmail(String email) {
        boolean validateEmail=true;

        if (email.matches(emailPattern)) {
            validateEmail = true;
        }else {
            validateEmail = false;
        }

        return validateEmail;
    }


    @SuppressWarnings("deprecation")
    @Override
    public void successRegistration(Object objectType) {
        dismissProgressDialog();
        SignUpResponse signUpResponse = (SignUpResponse) objectType;
        if(signUpResponse != null){
            if(signUpResponse.getSuccess()){
                AlertDialog alertDialog = new AlertDialog.Builder(SignupView.this,AlertDialog.THEME_HOLO_LIGHT).create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setMessage(signUpResponse.getSuccessMessage());
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent=new Intent(SignupView.this,LoginView.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        firstName.setText("");
        lastName.setText("");
        userName.setText("");
        emailId.setText("");
        mobileNumber.setText("");
        password.setText("");
        confirmPassword.setText("");
        dob.setText("");
        address.setText("");
        city.setText("");
        state.setText("");
    }
}
