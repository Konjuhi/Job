package com.job.findjob.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


    public class GithubJob {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("url")
        @Expose
        public String url;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("company")
        @Expose
        public String company;
        @SerializedName("company_url")
        @Expose
        public String companyUrl;
        @SerializedName("location")
        @Expose
        public String location;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("how_to_apply")
        @Expose
        public String howToApply;
        @SerializedName("company_logo")
        @Expose
        public String companyLogo;
    }

