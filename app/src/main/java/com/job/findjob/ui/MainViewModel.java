package com.job.findjob.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.job.findjob.Utils;
import com.job.findjob.base.BaseViewModel;
import com.job.findjob.data.api.ConnectionServer;
import com.job.findjob.data.entity.GithubJob;
import com.job.findjob.data.storage.GithubJobRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends BaseViewModel<MainViewModel.Navigator> {

    MutableLiveData<List<GithubJob>> jobList = new MutableLiveData<>();

    public MainViewModel(Context context, ConnectionServer connectionServer, GithubJobRepository repository) {
        super(context, connectionServer, repository);
    }

    public LiveData<List<GithubJob>> getLiveData() {
        return getRepository().getLiveData();
    }

    public LiveData<List<GithubJob>> getLiveDataMarked() {
        return getRepository().getLiveDataMarked();
    }

    public LiveData<List<GithubJob>> searchLiveData(String keyword){
        return getRepository().searchLiveData(keyword);
    }

    public String formatDate(String date){
        return Utils.dateToTimeFormat(date);
    }

    public void getJobFromServer() {
        getNavigator().showProgress();
        getConnectionServer().getJobList().enqueue(new Callback<List<GithubJob>>() {
            @Override
            public void onResponse(Call<List<GithubJob>> call, Response<List<GithubJob>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    for (GithubJob item : response.body()) {
                        item.createdAt = Utils.dateFormatter(item.createdAt); // format date
                        getRepository().insert(item);
                    }
                    getNavigator().onGetResult(true, "Success");
                }
                getNavigator().hideProgress();
            }
            @Override
            public void onFailure(Call<List<GithubJob>> call, Throwable t) {
                t.getLocalizedMessage();
                getNavigator().hideProgress();
                getNavigator().onGetResult(false, Utils.errorMessageHandler(call, t));
            }
        });

    }

    public void searchJobFromServer(String keyword) {
        getNavigator().showProgress();
        getConnectionServer().searchJobList(keyword).enqueue(new Callback<List<GithubJob>>() {
            @Override
            public void onResponse(Call<List<GithubJob>> call, Response<List<GithubJob>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    for (GithubJob item : response.body()) {
                        getRepository().insert(item);
                    }
                    getNavigator().onGetResult(true, "Success");
                }
                getNavigator().hideProgress();
            }
            @Override
            public void onFailure(Call<List<GithubJob>> call, Throwable t) {
                t.getLocalizedMessage();
                getNavigator().hideProgress();
                getNavigator().onGetResult(false, Utils.errorMessageHandler(call, t));
            }
        });
    }

    public void markJob(GithubJob githubJob) {
        getRepository().updateMarkJob(githubJob);
        getNavigator().onMark(githubJob.is_mark, githubJob.title);
    }

    public void onItemClick(GithubJob githubJob){
        getNavigator().onItemClick(githubJob);
    }

    public static class ModelFactory implements ViewModelProvider.Factory {
        private Context context;
        private ConnectionServer server;
        private GithubJobRepository repository;
        public ModelFactory(Context context, ConnectionServer server, GithubJobRepository repository) {
            this.context = context;
            this.server = server;
            this.repository = repository;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MainViewModel(context, server, repository);
        }
    }

     public interface Navigator {
        void showProgress();
        void hideProgress();
        void onGetResult(boolean status, String message);
        void onMark(int mark, String title);
        void onItemClick(GithubJob githubJob);
    }
}