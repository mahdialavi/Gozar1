package com.af.silaa_grp.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallbackInfo {


        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("lastversion")
        @Expose
        public int lastversion;
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getActive() {
            return lastversion;
        }

        public void setActive(int active) {
            this.lastversion = active;
        }


}
