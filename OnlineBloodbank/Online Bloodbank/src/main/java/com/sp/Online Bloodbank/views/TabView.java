package com.sp.bookmarket.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sp.bookmarket.R;
import com.sp.bookmarket.constants.Constant;
import com.sp.bookmarket.database.DBAdapter;

/**
 * Created by savan_007 on 7/14/2016.
 */
@SuppressWarnings("ALL")
public class TabView extends FragmentActivity {

    FragmentTabHost mTabHost;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab_view);

        dbAdapter = BookMarketApplication.getApplication(this).getDBInstance();
        String userId = dbAdapter.getLoggedInUserDetail();
        BookMarketApplication.printLogMessage(3, userId);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);


        //create Bundle

        Bundle b = new Bundle();
        b.putString(Constant.KEY_LOGGED_IN_USER_ID, userId);

        mTabHost.addTab(mTabHost.newTabSpec("home").setIndicator("",getResources().getDrawable(R.drawable.home)),HomeTabView.class,b);
        mTabHost.addTab(mTabHost.newTabSpec("myAccount").setIndicator("", getResources().getDrawable(R.drawable.profile)), MyAccountTabView.class, b);
        mTabHost.addTab(mTabHost.newTabSpec("setting").setIndicator("", getResources().getDrawable(R.drawable.setting)), SettingTabView.class, b);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String arg0) {
                setTabColor(mTabHost);
            }
        });

        setTabColor(mTabHost);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private DBAdapter dbAdapter;

    public void setTabColor(TabHost tabhost) {

        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++)
            tabhost.getTabWidget().getChildAt(i).setBackground(getResources().getDrawable(R.color.dark_blue)); //unselected tab color
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackground(getResources().getDrawable(R.color.button_orange)); //selected tab color
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void logoutUser() {
        dbAdapter.logoutUser();
        Intent i = new Intent(this, LoginView.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}