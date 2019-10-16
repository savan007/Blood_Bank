package com.sp.bookmarket.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sp.bookmarket.R;
import com.sp.bookmarket.constants.Constant;
import com.sp.bookmarket.database.DBAdapter;

/**
 * Created by savan_007 on 7/10/2016.
 */
public class SplashView extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_view);
        dbAdapter= BookMarketApplication.getApplication(this).getDBInstance();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

                if(dbAdapter.isLoggedIn()) {
                    Intent intent = new Intent(SplashView.this, TabView.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashView.this, LoginView.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, Constant.SPLASH_TIMEOUT);
    }
    private DBAdapter dbAdapter;
    @Override
    public void onBackPressed()
    {

    }
}
