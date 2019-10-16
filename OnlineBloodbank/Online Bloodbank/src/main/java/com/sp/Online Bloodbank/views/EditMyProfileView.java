package com.sp.online bloodbank.views;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sp.bookmarket.R;
import com.sp.bookmarket.constants.Constant;
import com.sp.bookmarket.models.EditMyProfileResponse;
import com.sp.bookmarket.models.MyProfileResponse;
import com.sp.bookmarket.models.UploadImageResponse;
import com.sp.bookmarket.presenters.EditMyProfileListener;
import com.sp.bookmarket.presenters.EditMyProfilePresenter;
import com.sp.bookmarket.webservices.ChipsApi;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by savan_007 on 7/24/2016.
 */
public class EditMyProfileView extends Activity implements EditMyProfileListener{

    //bind
    @Bind(R.id.btn_upload_pic)
    Button uploadPic;

    @Bind(R.id.btn_choose_pic)
    Button choosePic;

    @Bind(R.id.edt_first_name)
    EditText editFirstName;

    @Bind(R.id.edt_Last_name)
    EditText editLastName;

    @Bind(R.id.edt_dob)
    TextView txtDob;

    @Bind(R.id.edt_mobile)
    EditText editMobile;

    @Bind(R.id.edt_address)
    EditText editAddress;

    @Bind(R.id.edt_city)
    EditText editCity;

    @Bind(R.id.edt_state)
    EditText editState;

    @Bind(R.id.profile_picture)
    CircleImageView profilePicture;

    String userId,firstName,lastName,dob,userName,email,mobileNumber,address,city,state;
    ProgressDialog progressDialog;
    private int PICK_IMAGE_REQUEST = 1;
    EditText editText;
    private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 1111;
    Uri imageUri;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_edit_my_profile);
        ButterKnife.bind(this);

        bookMarketApplication = BookMarketApplication.getApplication(this);
        chipsApi = bookMarketApplication.getChipsApi();
        editMyProfilePresenter = new EditMyProfilePresenter(this,this,chipsApi);

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
        actionBarTitle.setText(getApplicationContext().getString(R.string.custom_actionbar_title_edit_my_profile));

        //Back Button
        final ImageButton actionBarHomeButton = (ImageButton) findViewById(R.id.action_bar_home_button);
        actionBarHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditMyProfileView.this,MyProfileView.class);
                intent.putExtra(Constant.KEY_LOGGED_IN_USER_ID,userId);
                startActivity(intent);
                finish();
            }
        });

        //right click button
        final ImageButton actionBarActionButton = (ImageButton) findViewById(R.id.action_bar_action_button);

        actionBarActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bookMarketApplication.isInternetConnection()) {
                    showProgress();
                    firstName = editFirstName.getText().toString();
                    lastName = editLastName.getText().toString();
                    mobileNumber = editMobile.getText().toString();
                    address = editAddress.getText().toString();
                    city = editCity.getText().toString();
                    state = editState.getText().toString();
                    dob = txtDob.getText().toString();
                    editMyProfilePresenter.updateUser(userId, firstName, lastName, dob, mobileNumber, address, city, state);

                }else {
                    Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.internet_connection_not_available), Toast.LENGTH_LONG).show();
                }

            }
        });

        // custom action bar finish

        userId = getIntent().getStringExtra(Constant.KEY_LOGGED_IN_USER_ID);
        if(bookMarketApplication.isInternetConnection()) {
            showProgress();
            editMyProfilePresenter.getUserInfo(userId);
        }else {
            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.internet_connection_not_available), Toast.LENGTH_LONG).show();
        }

    }

    private BookMarketApplication bookMarketApplication;
    private EditMyProfilePresenter editMyProfilePresenter;
    private ChipsApi chipsApi;

    @OnClick(R.id.btn_choose_pic)
    public void choosePicture()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                //Setting the Bitmap to ImageView
                profilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.btn_upload_pic)
    public void uploadPicture()
    {
        showProgress();
        editMyProfilePresenter.uploadImage(userId,imageUri);
    }

    @SuppressWarnings("deprecation")
    @OnClick(R.id.btn_birth_date)
    public void setBirthDate()
    {
        showDialog(DATE_PICKER_ID);
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
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            // Show selected date
            txtDob.setText(new StringBuilder().append(year)
                    .append("/").append(month + 1).append("/").append(day)
                    .append(" "));

        }
    };

    public void getDate(String oldDate)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date;
        String changedDate = null;
        try {
            if(oldDate!=null) {
                date = simpleDateFormat.parse(oldDate);
                month = Integer.parseInt((String) android.text.format.DateFormat.format("MM", date))-1;
                year = Integer.parseInt((String) android.text.format.DateFormat.format("yyyy", date)); //2013
                day = Integer.parseInt((String) android.text.format.DateFormat.format("dd", date)); //20
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void showProgress() {
        progressDialog = new ProgressDialog(EditMyProfileView.this, AlertDialog.THEME_HOLO_LIGHT);
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
    public void successEditProfile(Object objectType) {
        dismissProgressDialog();
        EditMyProfileResponse editMyProfileResponse = (EditMyProfileResponse) objectType;
        if(editMyProfileResponse != null && editMyProfileResponse.getSuccess()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(Constant.KEY_SUCCESS).setView(editText)
                    .setMessage(editMyProfileResponse.getSuccessMessage()).setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            Intent intent = new Intent(EditMyProfileView.this,MyProfileView.class);
                            intent.putExtra(Constant.KEY_LOGGED_IN_USER_ID,userId);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    @Override
    public void successViewProfile(Object objectType) {
        dismissProgressDialog();
        MyProfileResponse myProfileResponse = (MyProfileResponse) objectType;
        if(myProfileResponse != null && myProfileResponse.getSuccess()) {
            getDate(myProfileResponse.getUserProfile().getDob());
            editFirstName.setText(myProfileResponse.getUserProfile().getFirstName());
            editLastName.setText(myProfileResponse.getUserProfile().getLastName());
            editCity.setText(myProfileResponse.getUserProfile().getCity());
            editState.setText(myProfileResponse.getUserProfile().getState());
            editAddress.setText(myProfileResponse.getUserProfile().getAddress());
            editMobile.setText(myProfileResponse.getUserProfile().getMobileNumber());
            txtDob.setText(myProfileResponse.getUserProfile().getDob());
            Picasso.with(getApplicationContext())
                    .load(TextUtils.isEmpty(Constant.IMAGE_PATH+myProfileResponse.getUserProfile().getProfilePicture()) ? null : Constant.IMAGE_PATH+myProfileResponse.getUserProfile().getProfilePicture())
                    .placeholder(R.drawable.profile_picture)
                    .error(R.drawable.profile_picture)
                    .fit()
                    .into(profilePicture);
        }
    }

    @Override
    public void successUploadImage(Object objectType) {
        dismissProgressDialog();
        UploadImageResponse uploadImageResponse = (UploadImageResponse) objectType;
        if(uploadImageResponse != null && uploadImageResponse.getSuccess()) {
            Toast.makeText(getApplicationContext(), uploadImageResponse.getSuccessMessage(), Toast.LENGTH_LONG).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(Constant.KEY_SUCCESS).setView(editText)
                    .setMessage(uploadImageResponse.getSuccessMessage()).setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            if(bookMarketApplication.isInternetConnection()) {
                                showProgress();
                                editMyProfilePresenter.getUserInfo(userId);
                            }else {
                                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.internet_connection_not_available), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}