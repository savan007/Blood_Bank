package com.sp.bookmarket.views;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sp.bookmarket.R;
import com.sp.bookmarket.constants.Constant;

/**
 * Created by savan_007 on 11/22/2016.
 */
public class HelpView extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        //Custom Actionbar Start
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_custom_actionbar, null);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        //View Name
        final TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText(getApplicationContext().getString(R.string.custom_actionbar_title_help));

        //Back Button
        final ImageButton actionBarHomeButton = (ImageButton) findViewById(R.id.action_bar_home_button);
        actionBarHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //right click button
        final ImageButton actionBarActionButton = (ImageButton) findViewById(R.id.action_bar_action_button);
        actionBarActionButton.setVisibility(View.GONE);
        // custom action bar finish

    }
}
