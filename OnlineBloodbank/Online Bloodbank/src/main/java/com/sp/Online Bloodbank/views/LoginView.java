package com.sp.bookmarket.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sp.bookmarket.R;
import com.sp.bookmarket.constants.Constant;
import com.sp.bookmarket.database.DBAdapter;
import com.sp.bookmarket.models.LoginResponse;
import com.sp.bookmarket.presenters.LoginViewListener;
import com.sp.bookmarket.presenters.LoginPresenter;
import com.sp.bookmarket.webservices.ChipsApi;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginView extends Activity implements LoginViewListener {

    @Bind(R.id.user_name_login)
    EditText userName;

    @Bind(R.id.password_login)
    EditText password;

/*
    @Bind(R.id.button_demo)
    Button demo;
*/

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        bookMarketApplication=BookMarketApplication.getApplication(this);
        chipsApi = bookMarketApplication.getChipsApi();
        mainActivityPresenter=new LoginPresenter(this,this,chipsApi);
        dbAdapter = bookMarketApplication.getDBInstance();

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    performLogin();
                    handled = true;
                }
                return handled;
            }
        });
    }

    private BookMarketApplication bookMarketApplication;
    private LoginPresenter mainActivityPresenter;
    private ChipsApi chipsApi;
    private DBAdapter dbAdapter;


    @OnClick(R.id.button_login)
    public void performLogin() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        if (bookMarketApplication.isInternetConnection()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgress();
                    mainActivityPresenter.doLogin(userName.getText().toString(),password.getText().toString());
                }
            });
        } else {
            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.internet_connection_not_available), Toast.LENGTH_LONG).show();
        }

    }

    @OnClick(R.id.button_sign_up)
    public void signUp() {
        Intent intent=new Intent(LoginView.this,SignupView.class);
        startActivity(intent);
    }

    @SuppressWarnings("deprecation")
    private void showProgress() {
        progressDialog = new ProgressDialog(LoginView.this, AlertDialog.THEME_HOLO_LIGHT);
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
        AlertDialog alertDialog = new AlertDialog.Builder(LoginView.this,AlertDialog.THEME_HOLO_LIGHT).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setMessage(message);
        alertDialog.setButton(Constant.KEY_ALERT_OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    @Override
    public void successLogin(Object objectType) {
        LoginResponse loginResponse = (LoginResponse) objectType;
        if(loginResponse != null && loginResponse.getSuccess()) {
            String userId = dbAdapter.addNewUser(loginResponse);
            dismissProgressDialog();
            Intent intent=new Intent(LoginView.this,TabView.class);
            startActivity(intent);
        }

        //TODO handle login response
    }

    @Override public void onPause() {
        super.onPause();
        password.setText("");
        password.clearFocus();
    }

}
