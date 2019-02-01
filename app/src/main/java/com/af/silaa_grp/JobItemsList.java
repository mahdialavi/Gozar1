package com.af.silaa_grp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JobItemsList {

    @SerializedName("name")
    @Expose
    public String name = null;

    @SerializedName("tarikh")
    @Expose
    public String tarikh;
    @SerializedName("address")
    @Expose
    public String address;


    @SerializedName("tamas")
    @Expose
    public String tamas;

    @SerializedName("categoryname")
    @Expose
    public String categoryname;

    @SerializedName("parent_id")
    @Expose
    public int parent_id;



    @SerializedName("vizhe")
    @Expose
    public int vizhe;

    @SerializedName("tozihat")
    @Expose
    public String tozihat;

    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("image2")
    @Expose
    public String image2;
    @SerializedName("image3")
    @Expose
    public String image3;

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("published")
    @Expose
    public int published;

    @SerializedName("totalposts")
    @Expose
    public int totalposts;
    @SerializedName("buisinesPics")
    @Expose
    public ArrayList<String> buisinesPics = null;


    @SerializedName("ads_id")
    @Expose
    public int ads_id;

    @SerializedName("ads_image")
    @Expose
    public String ads_image;

    @SerializedName("ads_name")

    @Expose
    public String ads_name;




}
