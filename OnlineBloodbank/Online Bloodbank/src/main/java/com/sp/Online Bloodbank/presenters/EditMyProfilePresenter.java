package com.sp.bookmarket.presenters;

import android.content.Context;
import android.net.Uri;

import com.sp.bookmarket.models.EditMyProfileResponse;
import com.sp.bookmarket.models.MyProfileResponse;
import com.sp.bookmarket.models.UploadImageResponse;
import com.sp.bookmarket.webservices.ChipsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

/**
 * Created by savan_007 on 7/24/2016.
 */
public class EditMyProfilePresenter {

    private EditMyProfileListener mEditMyProfileListener;
    private ChipsApi mChipsApi;
    private Context mContext;

    public EditMyProfilePresenter(Context context, EditMyProfileListener editMyProfileListener, ChipsApi chipsApi) {

        this.mContext = context;
        this.mEditMyProfileListener = editMyProfileListener;
        this.mChipsApi = chipsApi;
    }

    public void updateUser(String userId,String firstName,String lastName,String dob,String mobileNumber,String address,String city,String state){
        if (userId != null && userId.trim().length() == 0) {
            mEditMyProfileListener.validateCredential("Please check UserId");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", userId);
            jsonObject.put("firstname",firstName );
            jsonObject.put("lastname", lastName);
            jsonObject.put("dob", dob);
            jsonObject.put("mobilenumber", mobileNumber);
            jsonObject.put("address", address);
            jsonObject.put("city", city);
            jsonObject.put("state", state);

        } catch (JSONException e) {
            BookMarketApplication.printLogMessage(5, "JSONException");
            return;
        }
        try {
            String json = jsonObject.toString();
            TypedInput typedInput = new TypedByteArray("application/json", json.getBytes("UTF-8"));
            mChipsApi.bmUpdateProfile(typedInput, new Callback<EditMyProfileResponse>()
            {
                @Override
                public void success(EditMyProfileResponse editMyProfileResponse, Response response)
                {
                    if(editMyProfileResponse != null && editMyProfileResponse.getStatus()==200) {
                        mEditMyProfileListener.successEditProfile(editMyProfileResponse);
                    } else {
                        mEditMyProfileListener.validateCredential(editMyProfileResponse.getSuccessMessage());
                    }
                }

                @Override
                public void failure(RetrofitError error)
                {
                    mEditMyProfileListener.validateCredential(error.getMessage());
                }
            });
        } catch(UnsupportedEncodingException e) {
            mEditMyProfileListener.validateCredential("Problem in calling webservice");
        }
    }

    public void uploadImage(String userId,Uri filePath){
        if (userId != null && userId.trim().length() == 0) {
            mEditMyProfileListener.validateCredential("Please check UserId");
            return;
        }
        TypedFile typedFile = new TypedFile("",new File(filePath.getPath()));

        mChipsApi.bmUploadImage(typedFile,userId, new Callback<UploadImageResponse>()
        {
            @Override
            public void success(UploadImageResponse uploadImageResponse, Response response)
            {
                if(uploadImageResponse != null && uploadImageResponse.getStatus()==200) {
                    mEditMyProfileListener.successUploadImage(uploadImageResponse);
                } else {
                    mEditMyProfileListener.validateCredential(uploadImageResponse.getSuccessMessage());
                }
            }

            @Override
            public void failure(RetrofitError error)
            {
                mEditMyProfileListener.validateCredential(error.getMessage());
            }
        });

    }

    public void getUserInfo(String userId){
        if (userId != null && userId.trim().length() == 0) {
            mEditMyProfileListener.validateCredential("Please check UserId");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", Integer.parseInt(userId));

        } catch (JSONException e) {
            BookMarketApplication.printLogMessage(5, "JSONException");
            return;
        }
        try {
            String json = jsonObject.toString();
            TypedInput typedInput = new TypedByteArray("application/json", json.getBytes("UTF-8"));
            mChipsApi.bmMyProfile(typedInput, new Callback<MyProfileResponse>()
            {
                @Override
                public void success(MyProfileResponse myProfileResponse, Response response)
                {
                    if(myProfileResponse != null && myProfileResponse.getStatus()==200) {
                        mEditMyProfileListener.successViewProfile(myProfileResponse);
                    } else {
                        mEditMyProfileListener.validateCredential(myProfileResponse.getSuccessMessage());
                    }
                }

                @Override
                public void failure(RetrofitError error)
                {
                    mEditMyProfileListener.validateCredential(error.getMessage());
                }
            });
        } catch(UnsupportedEncodingException e) {
            mEditMyProfileListener.validateCredential("Problem in calling webservice");
        }
    }
}
