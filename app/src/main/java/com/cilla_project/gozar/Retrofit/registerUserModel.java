package com.cilla_project.gozar.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class registerUserModel {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("pass")
    @Expose
    private String pass;

    @SerializedName("name")
    @Expose
    public String name;

    @Expose
    @SerializedName("response")
    public String response;
    @Expose
    @SerializedName("userId")
    public int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getResponse() {
        return response;
    }


    public String getName() {
        return name;
    }
}
