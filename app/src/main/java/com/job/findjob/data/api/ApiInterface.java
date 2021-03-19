package com.job.findjob.data.api;

import com.job.findjob.data.entity.GithubJob;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("positions.json")
    Call<List<GithubJob>> getJobList();
}
