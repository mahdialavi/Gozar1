package com.cilla_project.gozar.Retrofit;


import com.cilla_project.gozar.JobItemsList;

public interface UploadPosts {

    void AnswerBase(ItemsListUpload answer);
    void SendError(Throwable t);
}
