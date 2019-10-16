package com.sp.bookmarket.presenters;

import android.content.Context;

import com.sp.bookmarket.models.ViewAllPostResponse;
import com.sp.bookmarket.webservices.ChipsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * Created by savan_007 on 8/23/2016.
 */
public class HomeTabViewPresenter {
    private HomeTabViewListener mHomeTabViewListener;
    private ChipsApi mChipsApi;
    private Context mContext;

    public HomeTabViewPresenter(Context context, HomeTabViewListener homeTabViewListener, ChipsApi chipsApi) {
        this.mContext = context;
        this.mHomeTabViewListener = homeTabViewListener;
        this.mChipsApi = chipsApi;
    }

    public void viewAllPost(String userId){
        if (userId != null && userId.trim().length() == 0) {
            mHomeTabViewListener.validateCredential("Please check UserId");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userid", Integer.parseInt(userId));

        } catch (JSONException e) {
            BookMarketApplication.printLogMessage(5, "JSONException");
            return;
        }
        try {
            String json = jsonObject.toString();
            TypedInput typedInput = new TypedByteArray("application/json", json.getBytes("UTF-8"));
            mChipsApi.bmViewAllPost(typedInput, new Callback<ViewAllPostResponse>()
            {
                @Override
                public void success(ViewAllPostResponse viewAllPostResponse, Response response)
                {
                    if(viewAllPostResponse != null && viewAllPostResponse.getStatus()==200) {
                        mHomeTabViewListener.successAllPost(viewAllPostResponse);
                    } else {
                        mHomeTabViewListener.validateCredential(viewAllPostResponse.getSuccessMessage());
                    }
                }

                @Override
                public void failure(RetrofitError error)
                {
                    mHomeTabViewListener.validateCredential(error.getMessage());
                }
            });
        } catch(UnsupportedEncodingException e) {
            mHomeTabViewListener.validateCredential("Problem in calling webservice");
        }
    }
}
