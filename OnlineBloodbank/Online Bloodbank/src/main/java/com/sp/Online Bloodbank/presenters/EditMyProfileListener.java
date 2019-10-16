package com.sp.bookmarket.presenters;

/**
 * Created by savan_007 on 7/24/2016.
 */
public interface EditMyProfileListener {
    void validateCredential(String message);
    void successEditProfile(Object objectType);
    void successViewProfile(Object objectType);
    void successUploadImage(Object objectType);
}
