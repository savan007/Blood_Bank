package com.sp.bookmarket.views;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sp.bookmarket.R;
import com.sp.bookmarket.constants.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by savan_007 on 7/14/2016.
 */
public class SettingTabView extends Fragment {

    @Bind(R.id.btn_my_profile)
    Button myProfile;

    @Bind(R.id.btn_about_us)
    Button aboutUs;

    @Bind(R.id.btn_help)
    Button help;

    @Bind(R.id.btn_logout)
    Button logout;

    String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings_tab_view, container, false);

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
        actionBarTitle.setText(getActivity().getString(R.string.custom_actionbar_title_settings));

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

        return view;
    }

    @OnClick(R.id.btn_my_profile)
    public void myProfile() {

        if(userId!=null && userId.trim().length()>0)
        {
            Intent intent=new Intent(getActivity(),MyProfileView.class);
            intent.putExtra(Constant.KEY_LOGGED_IN_USER_ID,userId);
            startActivity(intent);
        }
    }

    @OnClick(R.id.btn_about_us)
    public void aboutUs() {
        Intent intent=new Intent(getActivity(),AboutUsView.class);
        startActivity(intent);
    }
    @OnClick(R.id.btn_help)
    public void help() {
        Intent intent=new Intent(getActivity(),HelpView.class);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof TabView) {
            tabView = (TabView) context;
        }
    }
    private TabView tabView;

    @OnClick(R.id.btn_logout)
    public void logout() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        if(tabView != null) {
                            tabView.logoutUser();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(getActivity().getString(R.string.alert_dialog_settings_title));
        builder.setMessage(getActivity().getString(R.string.alert_dialog_settings_message))
                .setPositiveButton(Constant.KEY_ALERT_YES, dialogClickListener)
                .setNegativeButton(Constant.KEY_ALERT_NO, dialogClickListener).show();
    }
}