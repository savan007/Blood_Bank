package com.sp.bookmarket.presenters;

import android.content.Context;

import com.sp.bookmarket.models.ChangePasswordResponse;
import com.sp.bookmarket.webservices.ChipsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * Created by savan_007 on 10/16/2016.
 */
public class ChangePasswordPresenter {
    private ChangePasswordListener mChangePasswordListener;
    private ChipsApi mChipsApi;
    private Context mContext;

    public ChangePasswordPresenter(Context context, ChangePasswordListener changePasswordListener, ChipsApi chipsApi)
    {
        this.mContext = context;
        this.mChangePasswordListener = changePasswordListener;
        this.mChipsApi = chipsApi;
    }

    public void changePassword(String userName,String userId,String oldPassword,String newPassword,String confirmPassword){

        if (oldPassword != null && oldPassword.trim().length() == 0) {
            mChangePasswordListener.validateCredential("Please enter Old Password");
            return;
        }

        if (newPassword != null && newPassword.trim().length() == 0) {
            mChangePasswordListener.validateCredential("Please enter New Password");
            return;
        }

        if (confirmPassword != null && confirmPassword.trim().length() == 0) {
            mChangePasswordListener.validateCredential("Please enter Confirm Password");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("ws_attribute_name",'attribute_value');
            jsonObject.put("user_name", userName);
            jsonObject.put("password", oldPassword);
            jsonObject.put("newpassword", newPassword);
        } catch (JSONException e) {
            BookMarketApplication.printLogMessage(5, "JSONException");
            return;
        }

        try {
            String json = jsonObject.toString();
            TypedInput typedInput = new TypedByteArray("application/json", json.getBytes("UTF-8"));
            mChipsApi.bmChangePassword(typedInput, new Callback<ChangePasswordResponse>()
            {
                @Override
                public void success(ChangePasswordResponse changePasswordResponse, Response response)
                {
                    if(changePasswordResponse != null && changePasswordResponse.getStatus()==200) {
                        mChangePasswordListener.successChangePassword(changePasswordResponse);
                    } else {
                        mChangePasswordListener.validateCredential(changePasswordResponse.getSuccessMessage());
                    }
                }

                @Override
                public void failure(RetrofitError error)
                {
                    mChangePasswordListener.validateCredential(error.getMessage());
                }
            });
        } catch(UnsupportedEncodingException e) {
            mChangePasswordListener.validateCredential("Problem in calling webservice");
        }

    }
}
