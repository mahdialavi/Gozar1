package com.af.silaa_grp.Retrofit;

import com.af.silaa_grp.JobItemsList;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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


    @Multipart
    @POST("upload_file.php")
    Call<ItemsListUpload> uploadfile(
            @Part("userid") RequestBody userid,
            @Part("catid") RequestBody catid,
            @Part("citycode") RequestBody citycode,
            @Part("code") RequestBody code,
            @Part("title") RequestBody title,
            @Part("mobile") RequestBody mobile,
            @Part("tozih") RequestBody tozih,
            @Part("address") RequestBody address,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3
//
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
    @POST("checkupdate.php")
    Call<CallbackInfo> checkupdate(
            @Field("versioncode") int versioncode

    );


    @GET("allitems.php")
    Call<ArrayList<JobItemsList>> getListProduct(
            @Query("command") String command,
            @Query("page") int page,
            @Query("citycode") int citycode,
            @Query("catid") int catid
    );

    @GET("management.php")
    Call<ArrayList<JobItemsList>> management(
            @Query("command") String command,
            @Query("page") int page,
            @Query("citycode") int citycode,
            @Query("catid") int catid
    );


    @GET("get_ads_list.php")
    Call<ArrayList<JobItemsList>> getadslist(
            @Query("page") int page,
            @Query("count") int count,
            @Query("q") String query,
            @Query("category_id") int category_id
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
    @GET("gettabligh_details.php")
    Call<ArrayList<JobItemsList>> getTablighItem(
            @Query("id") int id
    );

    @GET("get_ads_list.php")
    Call<ArrayList<JobItemsList>> get_ads_list(
            @Query("page") int page,
            @Query("count") int count,
            @Query("q") String query,
            @Query("cityid") int cityid
    );
}
