package com.sp.bookmarket.presenters;

import android.content.Context;

import com.sp.bookmarket.models.MyProfileResponse;
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
 * Created by savan_007 on 7/23/2016.
 */
public class MyProfilePresenter {

    private MyProfileListener mMyProfileListener;
    private ChipsApi mChipsApi;
    private Context mContext;

    public MyProfilePresenter(Context context, MyProfileListener myProfileListener, ChipsApi chipsApi) {

        this.mContext = context;
        this.mMyProfileListener = myProfileListener;
        this.mChipsApi = chipsApi;
    }


    public void userInfo(String userId){
        if (userId != null && userId.trim().length() == 0) {
            mMyProfileListener.validateCredential("Please check UserId");
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
                        mMyProfileListener.successMyProfile(myProfileResponse);
                    } else {
                        mMyProfileListener.validateCredential(myProfileResponse.getSuccessMessage());
                    }
                }

                @Override
                public void failure(RetrofitError error)
                {
                    mMyProfileListener.validateCredential(error.getMessage());
                }
            });
        } catch(UnsupportedEncodingException e) {
            mMyProfileListener.validateCredential("Problem in calling webservice");
        }

    }
}

