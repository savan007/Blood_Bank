package com.sp.bookmarket.views;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sp.bookmarket.R;
import com.sp.bookmarket.adapter.AllPostListAdapter;
import com.sp.bookmarket.constants.Constant;
import com.sp.bookmarket.models.ViewAllPostList;
import com.sp.bookmarket.models.ViewAllPostResponse;
import com.sp.bookmarket.presenters.HomeTabViewListener;
import com.sp.bookmarket.presenters.HomeTabViewPresenter;
import com.sp.bookmarket.webservices.ChipsApi;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by savan_007 on 7/14/2016.
 */

public class HomeTabView extends Fragment implements HomeTabViewListener {

    @Bind(R.id.list_all_post)
    ListView listViewAllPost;

    ProgressDialog progressDialog;
    String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        bookMarketApplication = BookMarketApplication.getApplication(getActivity());
        chipsApi = bookMarketApplication.getChipsApi();
        homeTabViewPresenter = new HomeTabViewPresenter(getActivity(),this,chipsApi);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_tab_view, container, false);

        ButterKnife.bind(this, view);

        //Custom Actionbar Start

        final ViewGroup actionBarLayout = (ViewGroup) inflater.inflate(R.layout.activity_custom_actionbar,container,false);

        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        //View Name
        final TextView actionBarTitle = (TextView) getActivity().findViewById(R.id.action_bar_title);
        actionBarTitle.setText(getActivity().getString(R.string.custom_actionbar_title_view_all_post));

        //Back Button
        final ImageButton actionBarHomeButton = (ImageButton) getActivity().findViewById(R.id.action_bar_home_button);
        actionBarHomeButton.setVisibility(View.GONE);

        //right click button
        final ImageButton actionBarActionButton = (ImageButton) getActivity().findViewById(R.id.action_bar_action_button);
        actionBarActionButton.setVisibility(View.GONE);

        // custom action bar finish

        if (getArguments() != null) {
            userId = getArguments().getString(Constant.KEY_LOGGED_IN_USER_ID);
        }

        if(bookMarketApplication.isInternetConnection()) {
            showProgress();
            homeTabViewPresenter.viewAllPost(userId);
        }else {
            Toast.makeText(getActivity(),getActivity().getString(R.string.internet_connection_not_available), Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private BookMarketApplication bookMarketApplication;
    private HomeTabViewPresenter homeTabViewPresenter;
    private ChipsApi chipsApi;



    @SuppressWarnings("deprecation")
    private void showProgress() {
        progressDialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait...!");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void validateCredential(String message) {
        dismissProgressDialog();
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void successAllPost(Object objectType) {
        dismissProgressDialog();
        listViewAllPost.setAdapter(null);
        final ViewAllPostResponse viewAllPostResponse = (ViewAllPostResponse) objectType;
        if(viewAllPostResponse != null && viewAllPostResponse.getSuccess()) {
            final List<ViewAllPostList> viewAllPostList = viewAllPostResponse.getViewAllPostList();
            if(viewAllPostList!=null) {
                listViewAllPost.setAdapter(new AllPostListAdapter(getActivity(), viewAllPostList));
                listViewAllPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(),AllPostDetailView.class);
                        intent.putExtra(Constant.KEY_SUBJECT,viewAllPostList.get(position).getSubject());
                        intent.putExtra(Constant.KEY_BOOK_NAME,viewAllPostList.get(position).getBookName());
                        intent.putExtra(Constant.KEY_AUTHOR_NAME,viewAllPostList.get(position).getAuthorName());
                        intent.putExtra(Constant.KEY_COST,viewAllPostList.get(position).getPrice());
                        intent.putExtra(Constant.KEY_DESCRIPTION,viewAllPostList.get(position).getDescription());
                        intent.putExtra(Constant.KEY_BOOK_IMAGE,viewAllPostList.get(position).getBookImage());
                        intent.putExtra(Constant.KEY_CATEGORY,viewAllPostList.get(position).getCategory());
                        intent.putExtra(Constant.KEY_FIRST_NAME,viewAllPostList.get(position).getFirstName());
                        intent.putExtra(Constant.KEY_LAST_NAME,viewAllPostList.get(position).getLastName());
                        intent.putExtra(Constant.KEY_ADDRESS,viewAllPostList.get(position).getAddress());
                        intent.putExtra(Constant.KEY_CITY,viewAllPostList.get(position).getCity());
                        intent.putExtra(Constant.KEY_STATE,viewAllPostList.get(position).getState());
                        intent.putExtra(Constant.KEY_MOBILE_NUMBER,viewAllPostList.get(position).getMobileNumber());
                        intent.putExtra(Constant.KEY_EMAIL_ID,viewAllPostList.get(position).getEmailId());
                        startActivity(intent);
                    }
                });
            }
        }
    }
}