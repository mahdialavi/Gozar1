package com.cilla_project.gozar.Retrofit;

import com.cilla_project.gozar.JobItemsList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {



    @GET("index.php")
    Call<ArrayList<JobItemsList>> getPosts();

    @GET("getPosts.php")
    Call<ArrayList<JobItemsList>> getPosts(
            @Query("page") int page,
            @Query("count") int count
    );

    @FormUrlEncoded
    @POST("search.php")
    Call<ArrayList<JobItemsList>> getPostsSearch(
            @Field("command") String command,
            @Field("text") String text,
            @Field("citycode") int citycode,
            @Field("page") int page

    );


    @FormUrlEncoded
    @POST("upload.php")
    Call<ItemsListUpload> uploadToServer(

            @Field("id") int id,
            @Field("code") int code,
            @Field("userId") int userId,
            @Field("catid") int catid,
            @Field("citycode") int citycode,
            @Field("title") String title,
            @Field("imglogo") String imglogo,
            @Field("mobile") String mobile,
            @Field("address") String address,
            @Field("tozih") String tozih

    );
    @FormUrlEncoded
    @POST("registeruser.php")
    Call<registerUserModel> registerUser(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("name") String name
    );

    @GET("registeruser.php")
    Call<registerMobileModel> registerSms(
            @Query("command") String command,
            @Query("mobile") String mobile,
            @Query("name") String name,
            @Query("code") String code
    );
    @FormUrlEncoded
    @POST("getitems.php")
    Call<ArrayList<JobItemsList>> getPostsCat(@Field("cat") int cat);




//    @FormUrlEncoded
//    @POST("media.php")
//    Call<ArrayList<MediaList>> getMedia(@Field("type") String type);


    @FormUrlEncoded
    @POST("versioncode.php")
    Call<CallbackInfo> getVersion(
            @Field("versioncode") int versioncode);


    @GET("getjobcat.php")
    Call<ArrayList<JobItemsList>> getListProduct(
            @Query("command") String command,
            @Query("page") int page,
            @Query("citycode") int citycode,
            @Query("catid") int catid
    );
    @GET("allmyads.php")
    Call<ArrayList<JobItemsList>> getmyads(
            @Query("page") int page,
            @Query("count") int count,
            @Query("userId") int userId
    );
    @GET("getjobdetails.php")
    Call<ArrayList<JobItemsList>> getOneItem(
            @Query("id") int id
    );

}
