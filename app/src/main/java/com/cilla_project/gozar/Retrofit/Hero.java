package com.cilla_project.gozar.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hero {
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("website")
        @Expose
        private String website;

        public String getName() {
            return name;
        }

    public Hero(String name, String website) {
        this.name = name;
        this.website = website;
    }

    public void setName(String name) {
            this.name = name;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

}
