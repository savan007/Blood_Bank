package com.sp.bookmarket.views;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.bookmarket.R;
import com.sp.bookmarket.constants.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by savan_007 on 7/14/2016.
 */
public class MyAccountTabView extends Fragment {

    @Bind(R.id.linear_layout_post_ad)
    LinearLayout postAd;

    @Bind(R.id.linear_layout_view_post)
    LinearLayout viewPost;

    @Bind(R.id.linear_layout_remove_post)
    LinearLayout removePost;

    String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_account_tab_view, container, false);

        ButterKnife.bind(this, view);

        //Custom Actionbar Start
        final ViewGroup actionBarLayout = (ViewGroup) inflater.inflate(R.layout.activity_custom_actionbar, null);

        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        //View Name
        final TextView actionBarTitle = (TextView) getActivity().findViewById(R.id.action_bar_title);
        actionBarTitle.setText(getActivity().getString(R.string.custom_actionbar_title_my_account));

        //Back Button
        final ImageButton actionBarHomeButton = (ImageButton) getActivity().findViewById(R.id.action_bar_home_button);
        actionBarHomeButton.setVisibility(View.GONE);

        //right click button
        final ImageButton actionBarActionButton = (ImageButton) getActivity().findViewById(R.id.action_bar_action_button);
        actionBarActionButton.setVisibility(View.GONE);

        //Custom Actionbar Complete

        if (getArguments() != null) {
            userId = getArguments().getString(Constant.KEY_LOGGED_IN_USER_ID);
        }

        postAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),PostAdView.class);
                intent.putExtra(Constant.KEY_LOGGED_IN_USER_ID,userId);
                startActivity(intent);
            }
        });
        viewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MyPostView.class);
                intent.putExtra(Constant.KEY_LOGGED_IN_USER_ID,userId);
                startActivity(intent);
            }
        });
        removePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),RemovePostView.class);
                intent.putExtra(Constant.KEY_LOGGED_IN_USER_ID,userId);
                startActivity(intent);
            }
        });

        return view;
    }

}