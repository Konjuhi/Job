package com.job.findjob.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.job.findjob.Utils;
import com.job.findjob.base.BaseViewModel;
import com.job.findjob.data.api.ConnectionServer;
import com.job.findjob.data.entity.GithubJob;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel  extends BaseViewModel<MainViewModel.Navigator> {

    MutableLiveData<List<GithubJob>> jobList = new MutableLiveData<>();

    public MainViewModel(Context context, ConnectionServer connectionServer) {
        super(context, connectionServer);
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
                    jobList.postValue(response.body());
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

    public static class ModelFactory implements ViewModelProvider.Factory {
        private Context context;
        private ConnectionServer server;
        public ModelFactory(Context context, ConnectionServer server) {
            this.context = context;
            this.server = server;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MainViewModel(context, server);
        }
    }

    interface Navigator {
        void showProgress();
        void hideProgress();
        void onGetResult(boolean status, String message);
    }
}

