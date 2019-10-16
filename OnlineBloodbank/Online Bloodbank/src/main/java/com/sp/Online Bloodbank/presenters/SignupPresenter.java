package com.sp.bookmarket.presenters;

import android.content.Context;

import com.sp.bookmarket.models.SignUpResponse;
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
 * Created by savan_007 on 7/5/2016.
 */
public class SignupPresenter {
    private SignupListener mSignupListener;
    private ChipsApi mChipsApi;
    private Context mContext;

    public SignupPresenter(Context context, SignupListener signupListener, ChipsApi chipsApi)
    {
        this.mContext = context;
        this.mSignupListener = signupListener;
        this.mChipsApi = chipsApi;
    }

    public void registerUser(String firstName,String lastName,String userName,String emailId,String mobileNumber,String password,String dob,String address,String city,String state,String gender){

        if (firstName != null && firstName.trim().length() == 0) {
            mSignupListener.validateCredential("Please enter First Name");
            return;
        }
        if (lastName != null && lastName.trim().length() == 0) {
            mSignupListener.validateCredential("Please enter Last Name");
            return;
        }
        if (userName != null && userName.trim().length() == 0) {
            mSignupListener.validateCredential("Please enter Username");
            return;
        }
        if (emailId != null && emailId.trim().length() == 0) {
            mSignupListener.validateCredential("Please enter Email Address");
            return;
        }
        if (mobileNumber != null && mobileNumber.trim().length() == 0) {
            mSignupListener.validateCredential("Please enter Mobile Number");
            return;
        }
        if (password != null && password.trim().length() == 0) {
            mSignupListener.validateCredential("Please enter Password");
            return;
        }
        if (dob != null && dob.trim().length() == 0) {
            mSignupListener.validateCredential("Please enter Date of Birth");
            return;
        }
        if (address != null && address.trim().length() == 0) {
            mSignupListener.validateCredential("Please enter Address");
            return;
        }
        if (city != null && city.trim().length() == 0) {
            mSignupListener.validateCredential("Please enter City");
            return;
        }
        if (state != null && state.trim().length() == 0) {
            mSignupListener.validateCredential("Please enter State");
            return;
        }
        if (gender != null && gender.trim().length() == 0) {
            mSignupListener.validateCredential("Please enter Gender");
            return;
        }

        if(!mSignupListener.validateEmail(emailId))
        {
            mSignupListener.validateCredential("Please enter Valid email address");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("ws_attribute_name",'attribute_value');
            jsonObject.put("first_name", firstName);
            jsonObject.put("last_name", lastName);
            jsonObject.put("user_name", userName);
            jsonObject.put("email_id", emailId);
            jsonObject.put("mobile_number", mobileNumber);
            jsonObject.put("password", password);
            jsonObject.put("dob", dob);
            jsonObject.put("address", address);
            jsonObject.put("city", city);
            jsonObject.put("state", state);
            jsonObject.put("gender", gender);
        } catch (JSONException e) {
            BookMarketApplication.printLogMessage(5, "JSONException");
            return;
        }

        try {
            String json = jsonObject.toString();
            TypedInput typedInput = new TypedByteArray("application/json", json.getBytes("UTF-8"));
            mChipsApi.bmRegisterUser(typedInput, new Callback<SignUpResponse>()
            {
                @Override
                public void success(SignUpResponse signUpResponse, Response response)
                {
                    if(signUpResponse != null && signUpResponse.getStatus()==200) {
                        mSignupListener.successRegistration(signUpResponse);
                    } else {
                        mSignupListener.validateCredential(signUpResponse.getSuccessMessage());
                    }
                }

                @Override
                public void failure(RetrofitError error)
                {
                    mSignupListener.validateCredential(error.getMessage());
                }
            });
        } catch(UnsupportedEncodingException e) {
            mSignupListener.validateCredential("Problem in calling webservice");
        }

    }

}
