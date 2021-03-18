package com.job.findjob.data.api;

import javax.inject.Inject;

public class ConnectionServer {


    @Inject
    ApiInterface apiInterface;

    public ConnectionServer(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

}
