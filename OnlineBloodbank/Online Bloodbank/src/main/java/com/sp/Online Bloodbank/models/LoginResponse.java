package com.sp.bookmarket.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by savan_007 on 7/3/2016.
 */
public class LoginResponse {


    public String getSuccessMessage() {
        return successMessage;
    }

    public Boolean getSuccess() {
        return success;
    }

    public int getStatus() {
        return status;
    }

    public String getUser_id() {
        return user_id;
    }

    @SerializedName("success_message")
    private String successMessage;

    @SerializedName("success")
    private Boolean success;

    @SerializedName("status_Code")
    private int status;

    @SerializedName("user_id")
    private String user_id;

    public UserProfile getUserProfile() {
        return userProfile;
    }

    @SerializedName("UserInfo")
    private UserProfile userProfile;
}

