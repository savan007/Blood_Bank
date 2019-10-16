package com.sp.bookmarket.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by savan_007 on 7/17/2016.
 */
public class SignUpResponse {

    public String getSuccessMessage() {
        return successMessage;
    }

    public Boolean getSuccess() {
        return success;
    }

    public int getStatus() {
        return status;
    }

    @SerializedName("success_message")
    private String successMessage;

    @SerializedName("success")
    private Boolean success;

    @SerializedName("status_Code")
    private int status;

}
