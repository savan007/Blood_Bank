package com.sp.online bloodbank.webservices;
import android.content.res.Resources;

import retrofit.RestAdapter;

/**
 * Created by savan_007 on 7/3/2016.
 */
public class RestClient {

    public RestClient(Resources resources)
    {
        setRetrofit(resources);
    }

    //private static final String END_POINT = "http://192.168.1.119/API/";
    private static final String END_POINT = "http://172.20.10.7/API/";
    //private static final String END_POINT = "http://127.0.0.1:80/API/";
    //private static final String END_POINT = "http://bookmarket.site88.net/";
    //private static final String END_POINT = "http://uixservices.com/api";
    //private static final String END_POINT = "http://uttam.comze.com/api";

    private void setRetrofit(final Resources resources) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(END_POINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        chipsApi = restAdapter.create(ChipsApi.class);
    }

    private ChipsApi chipsApi = null;

    public ChipsApi getRestAPI()
    {
        return chipsApi;
    }
}



