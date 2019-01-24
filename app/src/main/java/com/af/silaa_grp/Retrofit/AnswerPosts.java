package com.af.silaa_grp.Retrofit;

import com.af.silaa_grp.JobItemsList;

import java.util.ArrayList;

/**
 * Created by root on 5/6/17.
 */

public interface AnswerPosts  {

    void AnswerBase(ArrayList<JobItemsList> answer);
    void SendError(Throwable t);
}
