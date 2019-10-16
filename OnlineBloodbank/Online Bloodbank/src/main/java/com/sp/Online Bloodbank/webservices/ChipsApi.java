package com.sp.online bloodbank.webservices;

import com.sp.bookmarket.models.ChangePasswordResponse;
import com.sp.bookmarket.models.EditMyProfileResponse;
import com.sp.bookmarket.models.LoginResponse;
import com.sp.bookmarket.models.MyProfileResponse;
import com.sp.bookmarket.models.PostAdResponse;
import com.sp.bookmarket.models.RemovePostResponse;
import com.sp.bookmarket.models.SignUpResponse;
import com.sp.bookmarket.models.UploadImageResponse;
import com.sp.bookmarket.models.MyPostResponse;
import com.sp.bookmarket.models.ViewAllPostResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

/**
 * Created by savan_007 on 7/3/2016.
 */
public interface ChipsApi {

    //Login
    @Headers({
            "Content-Type: application/json"
    })
    @POST("/login.php")
    void bmLogin(@Body TypedInput typedInput, Callback<LoginResponse> callback);

     //Register User
    @Headers({
            "Content-Type: application/json"
    })
    @POST("/register.php")
    void bmRegisterUser(@Body TypedInput typedInput, Callback<SignUpResponse> callback);

    //View Profile - My Profile
    @Headers({
            "Content-Type: application/json"
    })
    @POST("/myprofile.php")
    void bmMyProfile(@Body TypedInput typedInput, Callback<MyProfileResponse> callback);

    //Edit Profile - Update Profile
    @Headers({
            "Content-Type: application/json"
    })
    @POST("/updateinfo.php")
    void bmUpdateProfile(@Body TypedInput typedInput, Callback<EditMyProfileResponse> callback);

    //Upload profile Image
    @Multipart
    @POST("/imageupload.php")
    void bmUploadImage(@Part("myImageFile") TypedFile file,@Part("userid") String userId, Callback<UploadImageResponse> callback);

    //Post Ad
    //TODO add image
    @Headers({
            "Content-Type: application/json"
    })
    @POST("/postad.php")
    void bmPostAd(@Body TypedInput typedInput, Callback<PostAdResponse> callback);

    //View My Post
    @Headers({
            "Content-Type: application/json"
    })
    @POST("/viewmypost.php")
    void bmMyPost(@Body TypedInput typedInput, Callback<MyPostResponse> callback);

    //Change Password
    @Headers({
            "Content-Type: application/json"
    })
    @POST("/changepassword.php")
    void bmChangePassword(@Body TypedInput typedInput, Callback<ChangePasswordResponse> callback);

    //Remove Post
    @Headers({
            "Content-Type: application/json"
    })
    @POST("/removed.php")
    void bmRemovePost(@Body TypedInput typedInput, Callback<RemovePostResponse> callback);

    //View All Post
    @Headers({
            "Content-Type: application/json"
    })
    @POST("/viewallpost.php")
    void bmViewAllPost(@Body TypedInput typedInput, Callback<ViewAllPostResponse> callback);
}
