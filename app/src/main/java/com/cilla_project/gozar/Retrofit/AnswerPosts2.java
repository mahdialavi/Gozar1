package com.cilla_project.gozar.Retrofit;

import com.cilla_project.gozar.JobItemsList;

import java.util.ArrayList;

/**
 * Created by root on 5/6/17.
 */

public interface AnswerPosts2 {

    void AnswerBase(ArrayList<JobItemsList> answer);
    void SendError(Throwable t);
}
