package com.sp.bookmarket.presenters;

/**
 * Created by savan_007 on 7/5/2016.
 */
public interface SignupListener {
    void validateCredential(String message);
    boolean validateEmail(String email);
    void successRegistration(Object objectType);
}
