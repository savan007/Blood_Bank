package com.sp.bookmarket.views;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sp.bookmarket.R;
import com.sp.bookmarket.constants.Constant;
import com.sp.bookmarket.models.MyProfileResponse;
import com.sp.bookmarket.presenters.MyProfileListener;
import com.sp.bookmarket.presenters.MyProfilePresenter;
import com.sp.bookmarket.webservices.ChipsApi;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.PendingIntent.getActivity;

/**
 * Created by savan_007 on 7/23/2016.
 */
public class MyProfileView extends Activity implements MyProfileListener {

    @Bind(R.id.profile_picture)
    CircleImageView profilePicture;

    @Bind(R.id.txt_name)
    TextView name;

    @Bind(R.id.txt_dob)
    TextView dob;

    @Bind(R.id.txt_user_name)
    TextView user_name;

    @Bind(R.id.txt_email)
    TextView email;

    @Bind(R.id.txt_mobile)
    TextView mobile;

    @Bind(R.id.txt_address)
    TextView address;

    @Bind(R.id.txt_city)
    TextView city;

    @Bind(R.id.txt_state)
    TextView state;

    @Bind(R.id.btn_change_password)
    TextView change_password;

    String userId;
    ProgressDialog progressDialog;
    Bitmap bitmap;
    String profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);

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
        actionBarTitle.setText(getApplicationContext().getString(R.string.custom_actionbar_title_my_profile));

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
        actionBarActionButton.setImageResource(R.drawable.edit);
        actionBarActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MyProfileView.this,EditMyProfileView.class);
                intent.putExtra(Constant.KEY_LOGGED_IN_USER_ID,userId);
                startActivity(intent);
                finish();
            }
        });

        //actionBarActionButton.setVisibility(View.GONE);
        // custom action bar finish

        bookMarketApplication = BookMarketApplication.getApplication(this);
        chipsApi = bookMarketApplication.getChipsApi();
        myProfilePresenter = new MyProfilePresenter(this , this, chipsApi);

        userId = getIntent().getStringExtra(Constant.KEY_LOGGED_IN_USER_ID);
        if(bookMarketApplication.isInternetConnection()) {
            showProgress();
            myProfilePresenter.userInfo(userId);
        }else {
            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.internet_connection_not_available), Toast.LENGTH_LONG).show();
        }

    }

    private BookMarketApplication bookMarketApplication;
    private MyProfilePresenter myProfilePresenter;
    private ChipsApi chipsApi;

    @OnClick(R.id.btn_change_password)
    public void changePassword()
    {
        Intent intent = new Intent(MyProfileView.this,ChangePasswordView.class);
        intent.putExtra(Constant.KEY_LOGGED_IN_USER_ID,userId);
        intent.putExtra(Constant.KEY_USER_NAME,user_name.getText().toString());
        startActivity(intent);
    }

    @SuppressWarnings("deprecation")
    private void showProgress() {
        progressDialog = new ProgressDialog(MyProfileView.this, AlertDialog.THEME_HOLO_LIGHT);
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
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void successMyProfile(Object objectType) {
        dismissProgressDialog();
        MyProfileResponse myProfileResponse = (MyProfileResponse) objectType;
        if(myProfileResponse != null && myProfileResponse.getSuccess()) {
            name.setText(myProfileResponse.getUserProfile().getFirstName()+" "+myProfileResponse.getUserProfile().getLastName());
            dob.setText(myProfileResponse.getUserProfile().getDob());
            user_name.setText(myProfileResponse.getUserProfile().getUserName());
            email.setText(myProfileResponse.getUserProfile().getEmailId());
            mobile.setText(myProfileResponse.getUserProfile().getMobileNumber());
            state.setText(myProfileResponse.getUserProfile().getState());
            city.setText(myProfileResponse.getUserProfile().getCity());
            address.setText(myProfileResponse.getUserProfile().getAddress());

            Picasso.with(getApplicationContext())
                    .load(TextUtils.isEmpty(Constant.IMAGE_PATH+myProfileResponse.getUserProfile().getProfilePicture()) ? null : Constant.IMAGE_PATH+myProfileResponse.getUserProfile().getProfilePicture())
                    .placeholder(R.drawable.profile_picture)
                    .error(R.drawable.profile_picture)
                    .fit()
                    .into(profilePicture);

        }
    }

}