package com.cilla_project.gozar.Retrofit;

import android.util.Log;

import com.cilla_project.gozar.G;
import com.cilla_project.gozar.JobAdapter;
import com.cilla_project.gozar.JobItemsList;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Post {
//    public void getPosts(int page, final AnswerPosts answerPosts) {
//        Api service = ServiceGenerator.getClient().create(Api.class);
//        Call<ArrayList<JobItemsList>> call = service.getPosts(page, G.PRODUCT_PER_REQUEST);
//        call.enqueue(new Callback<ArrayList<JobItemsList>>() {
//            @Override
//            public void onResponse(Call<ArrayList<JobItemsList>> call, Response<ArrayList<JobItemsList>> response) {
//                ArrayList<JobItemsList> res = response.body();
//                answerPosts.AnswerBase(res);
//            }
//            @Override
//            public void onFailure(Call<ArrayList<JobItemsList>> call, Throwable t) {
//                answerPosts.SendError(t);
//                Log.e("getPosts:", t.getMessage());
//            }
//        });
//    }

    public void getProductList(String command,int page,int catid, final AnswerPosts answerPosts) {
        Api service = ServiceGenerator.getClient().create(Api.class);
        Call<ArrayList<JobItemsList>> call = service.getListProduct(command,page,catid);
        call.enqueue(new Callback<ArrayList<JobItemsList>>() {
            @Override
            public void onResponse(Call<ArrayList<JobItemsList>> call, Response<ArrayList<JobItemsList>> response) {
                ArrayList<JobItemsList> res = response.body();
                answerPosts.AnswerBase(res);
            }
            @Override
            public void onFailure(Call<ArrayList<JobItemsList>> call, Throwable t) {
                answerPosts.SendError(t);
                Log.e("getPosts:", t.getMessage());
            }
        });
    }

    public void getOneItem(int id, final AnswerPosts answerPosts) {
        Api service = ServiceGenerator.getClient().create(Api.class);
        Call<ArrayList<JobItemsList>> call = service.getOneItem(id);
        call.enqueue(new Callback<ArrayList<JobItemsList>>() {
            @Override
            public void onResponse(Call<ArrayList<JobItemsList>> call, Response<ArrayList<JobItemsList>> response) {
                ArrayList<JobItemsList> res = response.body();
                answerPosts.AnswerBase(res);
            }
            @Override
            public void onFailure(Call<ArrayList<JobItemsList>> call, Throwable t) {
                answerPosts.SendError(t);
                Log.e("getPosts:", t.getMessage());
            }
        });
    }
    public void getMyAds(int page,int userId, final AnswerPosts answerPosts) {
        Api service = ServiceGenerator.getClient().create(Api.class);
        Call<ArrayList<JobItemsList>> call = service.getmyads(page, G.PRODUCT_PER_REQUEST , userId);
        call.enqueue(new Callback<ArrayList<JobItemsList>>() {
            @Override
            public void onResponse(Call<ArrayList<JobItemsList>> call, Response<ArrayList<JobItemsList>> response) {
                ArrayList<JobItemsList> res = response.body();
                answerPosts.AnswerBase(res);
            }
            @Override
            public void onFailure(Call<ArrayList<JobItemsList>> call, Throwable t) {
                answerPosts.SendError(t);
                Log.e("getPosts:", t.getMessage());
            }
        });
    }



    public void requestInfo(int version,final CheckVersion checkVersion) {
        Api api = ServiceGenerator.getClient().create(Api.class);
        Call<CallbackInfo> callbackCall = api.getVersion(version);
        callbackCall.enqueue(new Callback<CallbackInfo>() {
            @Override
            public void onResponse(Call<CallbackInfo> call, Response<CallbackInfo> response) {
                CallbackInfo res=response.body();
//                if (res != null && res.getActive() != null) {
                    checkVersion.AnswerBase(res);
//                }
        }
            @Override
            public void onFailure(Call<CallbackInfo> call, Throwable t) {
                checkVersion.SendError(t);
            }
        });


    }
    public void uploadToServer(int id,int code,int userId,int  catid,String title,String image,String mobile,String address,String tozih,
                               final UploadPosts answerPosts) {
        Api service = ServiceGenerator.getClient().create(Api.class);
        Call<ItemsListUpload> call = service.uploadToServer(id,code,userId,catid,title,image,mobile,address,tozih);
//        id,code,userId,itemname,imglogo,email,website,manager,boss,mobile,phonenumber
        call.enqueue(new Callback<ItemsListUpload>() {
            @Override
            public void onResponse(Call<ItemsListUpload>call, Response<ItemsListUpload>response) {
                ItemsListUpload res= response.body();
                answerPosts.AnswerBase(res);
            }
            @Override
            public void onFailure(Call<ItemsListUpload> call, Throwable t) {
                answerPosts.SendError(t);
                Log.e("getPosts:", t.getMessage());
            }
        });
    }
    public void getPostsSearch(String command,String text,int page, final AnswerPosts answerPosts) {
        Api service = ServiceGenerator.getClient().create(Api.class);
        Call<ArrayList<JobItemsList>> call = service.getPostsSearch(command,text,page);
        call.enqueue(new Callback<ArrayList<JobItemsList>>() {
            @Override
            public void onResponse(Call<ArrayList<JobItemsList>> call, Response<ArrayList<JobItemsList>> response) {
                ArrayList<JobItemsList> res = response.body();
                answerPosts.AnswerBase(res);
            }
            @Override
            public void onFailure(Call<ArrayList<JobItemsList>> call, Throwable t) {
                answerPosts.SendError(t);
                Log.e("getPosts:", t.getMessage());
            }
        });
    }
//    public void registerUser(String email,String password,String name, final RegisgerUser answerPosts) {
//        Api service = ServiceGenerator.getClient().create(Api.class);
//        Call<registerUserModel> call = service.registerUser(email,password,name);
//        call.enqueue(new Callback<registerUserModel>() {
//            @Override
//            public void onResponse(Call<registerUserModel>call, Response<registerUserModel>response) {
//                registerUserModel res= response.body();
//                answerPosts.AnswerBase(res);
//            }
//            @Override
//            public void onFailure(Call<registerUserModel> call, Throwable t) {
//                answerPosts.SendError(t);
//                Log.e("getPosts:", t.getMessage());
//            }
//        });
//    }

    public void registerSms(String command,String mobile,String name,String code, final RegisteruserMobile answerPosts) {
        Api service = ServiceGenerator.getClient().create(Api.class);
        Call<registerMobileModel> call = service.registerSms(command,mobile,name,code);
        call.enqueue(new Callback<registerMobileModel>() {
            @Override
            public void onResponse(Call<registerMobileModel>call, Response<registerMobileModel>response) {
                registerMobileModel res= response.body();
                answerPosts.AnswerBase(res);
            }
            @Override
            public void onFailure(Call<registerMobileModel> call, Throwable t) {
                answerPosts.SendError(t);
                Log.e("getPosts:", t.getMessage());
            }
        });
    }

//    public void getPost(String id , final AnswerPost answerPost) {
//        Api service = ServiceGenerator.getClient().create(Api.class);
//        Call<PostModel> call = service.getPost(id);
//        call.enqueue(new Callback<PostModel>() {
//            @Override
//            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
//                PostModel res = response.body();
//                answerPost.AnswerBase(res);
//            }
//            @Override
//            public void onFailure(Call<PostModel> call, Throwable t) {
//                answerPost.SendError(t);
//                Log.e("getPost:", t.getMessage());
//            }
//        });
}
