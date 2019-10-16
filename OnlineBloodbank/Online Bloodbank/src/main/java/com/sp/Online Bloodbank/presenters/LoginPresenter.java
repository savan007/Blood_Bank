package com.sp.bookmarket.presenters;

import android.content.Context;

import com.sp.bookmarket.models.LoginResponse;
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
 * Created by savan_007 on 7/3/2016.
 */
public class LoginPresenter {

    private LoginViewListener mLoginViewListener;
    private ChipsApi mChipsApi;
    private Context mContext;

    public LoginPresenter(Context context, LoginViewListener loginViewListener, ChipsApi chipsApi)
    {
        this.mContext = context;
        this.mLoginViewListener = loginViewListener;
        this.mChipsApi = chipsApi;
    }

    public void doLogin(String userName,String password) {
        if (userName != null && userName.trim().length() == 0) {
            mLoginViewListener.validateCredential("Please enter UserName");
            return;
        }
        if (password != null && password.trim().length() == 0) {
            mLoginViewListener.validateCredential("Please enter Password");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", userName);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            BookMarketApplication.printLogMessage(5, "JSONException");
            return;
        }
        try {
            String json = jsonObject.toString();
            TypedInput typedInput = new TypedByteArray("application/json", json.getBytes("UTF-8"));
            mChipsApi.bmLogin(typedInput, new Callback<LoginResponse>()
            {
                @Override
                public void success(LoginResponse loginResponse, Response response)
                {
                    if(loginResponse != null && loginResponse.getStatus()==200) {
                        mLoginViewListener.successLogin(loginResponse);
                    } else {
                        mLoginViewListener.validateCredential(loginResponse.getSuccessMessage());
                    }
                }

                @Override
                public void failure(RetrofitError error)
                {
                    mLoginViewListener.validateCredential(error.getMessage());
                }
            });
        } catch(UnsupportedEncodingException e) {
            mLoginViewListener.validateCredential("Problem in calling webservice");
        }

    }
}
