package com.af.silaa_grp.Retrofit;


import com.af.silaa_grp.JobItemsList;

public interface UploadPosts {

    void AnswerBase(ItemsListUpload answer);
    void SendError(Throwable t);
}
