package com.sp.bookmarket.views;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sp.bookmarket.R;
import com.sp.bookmarket.constants.Constant;
import com.sp.bookmarket.models.ChangePasswordResponse;
import com.sp.bookmarket.presenters.ChangePasswordListener;
import com.sp.bookmarket.presenters.ChangePasswordPresenter;
import com.sp.bookmarket.webservices.ChipsApi;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by savan_007 on 10/16/2016.
 */
public class ChangePasswordView extends Activity implements ChangePasswordListener{

    @Bind(R.id.old_password)
    EditText oldPassword;

    @Bind(R.id.new_password)
    EditText newPassword;

    @Bind(R.id.confirm_password)
    EditText confirmPassword;

    ProgressDialog progressDialog;
    String userId,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        bookMarketApplication = BookMarketApplication.getApplication(this);
        chipsApi = bookMarketApplication.getChipsApi();
        changePasswordPresenter = new ChangePasswordPresenter(this, this, chipsApi);

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
        actionBarTitle.setText(getApplicationContext().getString(R.string.custom_actionbar_title_change_password));

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
        actionBarActionButton.setImageResource(R.drawable.action_done);
        actionBarActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //TODO change password validation and API call
                if (!checkPasswordAndConfirmPassword(newPassword.getText().toString(), confirmPassword.getText().toString())) {
                    confirmPassword.setError("New Password and Confirm password does not match");
                } else {
                    confirmPassword.setError(null);
                    if(bookMarketApplication.isInternetConnection()) {
                        showProgress();
                        changePasswordPresenter.changePassword(userName,userId,oldPassword.getText().toString(), newPassword.getText().toString(), confirmPassword.getText().toString());
                    }else {
                        Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.internet_connection_not_available), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //actionBarActionButton.setVisibility(View.GONE);
        // custom action bar finish

        userId = getIntent().getStringExtra(Constant.KEY_LOGGED_IN_USER_ID);
        userName = getIntent().getStringExtra(Constant.KEY_USER_NAME);

    }

    private BookMarketApplication bookMarketApplication;
    private ChangePasswordPresenter changePasswordPresenter;
    private ChipsApi chipsApi;


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

    @SuppressWarnings("deprecation")
    private void showProgress() {
        progressDialog = new ProgressDialog(ChangePasswordView.this, AlertDialog.THEME_HOLO_LIGHT);
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

    @Override
    public void validateCredential(String message) {
        dismissProgressDialog();
        //display alert dialog in case of null value of userName/password
        AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordView.this,AlertDialog.THEME_HOLO_LIGHT).create();
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
    public void successChangePassword(Object objectType) {
        dismissProgressDialog();
        ChangePasswordResponse changePasswordResponse = (ChangePasswordResponse) objectType;
        if(changePasswordResponse != null){
            if(changePasswordResponse.getSuccess()){
                AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordView.this,AlertDialog.THEME_HOLO_LIGHT).create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setMessage(changePasswordResponse.getSuccessMessage());
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                alertDialog.show();
            }
        }
    }

}
