package com.cilla_project.gozar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JobItemsList {

    @SerializedName("name")
    @Expose
    public String name=null;

    @SerializedName("tarikh")
    @Expose
    public String tarikh;


    @SerializedName("tamas")
    @Expose
    public String tamas;

    @SerializedName("categoryname")
    @Expose
    public String categoryname;

    @SerializedName("parent_id")
    @Expose
    public int parent_id;

    @SerializedName("tozihat")
    @Expose
    public String tozihat;

    @SerializedName("image")
    @Expose
    public String image;

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("totalposts")
    @Expose
    public int totalposts;
    @SerializedName("buisinesPics")
    @Expose
    public ArrayList<String> buisinesPics = null;




}
