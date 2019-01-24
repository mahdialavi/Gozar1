package com.af.silaa_grp.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class registerMobileModel {
    @SerializedName("command")
    @Expose
    public String command;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("code")
    @Expose
    public String code;

    @Expose
    @SerializedName("response")
    public String response;
    @Expose
    @SerializedName("userId")
    public int userId;

    @Expose
    @SerializedName("user_group")
    public int user_group;

}
