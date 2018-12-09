package com.cilla_project.gozar.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemsListUpload {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("imglogo")
    @Expose
    private String imglogo;
    @SerializedName("catid")
    @Expose
    private int catid;
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("lastId")
    @Expose
    private int lastId;

    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("tozih")
    @Expose
    private String tozih;

    @SerializedName("address")
    @Expose
    private String address;

    public int getLastId() {
        return lastId;
    }
    @SerializedName("response")
    public String response;

    public String getResponse() {
        return response;
    }
}
