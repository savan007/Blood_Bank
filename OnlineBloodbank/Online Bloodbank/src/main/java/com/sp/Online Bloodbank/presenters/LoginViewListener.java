package com.sp.bookmarket.presenters;

/**
 * Created by savan_007 on 7/3/2016.
 */
public interface LoginViewListener {
    void validateCredential(String message);
    void successLogin(Object objectType);

}
