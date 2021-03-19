package com.job.findjob.data.api;

import com.job.findjob.data.entity.GithubJob;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class ConnectionServer {


    @Inject
    ApiInterface apiInterface;

    public ConnectionServer(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public Call<List<GithubJob>> getJobList(){
        return apiInterface.getJobList();
    }

}
