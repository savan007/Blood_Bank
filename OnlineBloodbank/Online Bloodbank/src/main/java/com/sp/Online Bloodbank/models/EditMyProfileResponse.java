package com.sp.bookmarket.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by savan_007 on 7/24/2016.
 */
public class EditMyProfileResponse {
    @SerializedName("success_message")
    private String successMessage;

    @SerializedName("success")
    private Boolean success;

    @SerializedName("status_Code")
    private int status;

    public int getStatus() {
        return status;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public Boolean getSuccess() {
        return success;
    }
}
