package com.sp.bookmarket.presenters;

/**
 * Created by savan_007 on 10/16/2016.
 */
public interface ChangePasswordListener {
    void validateCredential(String message);
    void successChangePassword(Object objectType);
}
