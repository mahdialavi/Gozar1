package com.cilla_project.gozar.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallbackInfo {


        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("active")
        @Expose
        public int active;
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
        }


}
