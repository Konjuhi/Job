package com.job.findjob.base;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

import com.job.findjob.data.api.ConnectionServer;
import com.job.findjob.data.storage.GithubJobRepository;

import java.lang.ref.WeakReference;

public abstract class BaseViewModel<N> extends ViewModel {

    public static final String TAG = "vm -->";

    private final ObservableBoolean isLoading = new ObservableBoolean(false);
    private final ObservableBoolean isConnecting = new ObservableBoolean(false);
    private final ObservableBoolean isNetworkProblem = new ObservableBoolean(false);


    @SuppressLint("StaticFieldLeak")
    private Context context;
    private WeakReference<N> navigator;

    private ConnectionServer connectionServer;
    private GithubJobRepository repository;

    public BaseViewModel(Context context, ConnectionServer connectionServer,GithubJobRepository repository) {
        this.context = context;
        this.connectionServer = connectionServer;
        this.repository = repository;
    }

    public BaseViewModel(Context context) {
        this.context = context;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public ObservableBoolean getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading.set(isLoading);
    }

    public N getNavigator() {
        return navigator.get();
    }

    public void setNavigator(N navigator) {
        this.navigator = new WeakReference<>(navigator);
    }

    public ObservableBoolean getIsConnecting() {
        return isConnecting;
    }

    public void setIsConnecting(boolean isConnecting) {
        this.isConnecting.set(isConnecting);
    }

    public Context getContext() {
        return context;
    }

    public ConnectionServer getConnectionServer() {
        return connectionServer;
    }

    public GithubJobRepository getRepository(){
        return repository;
    }

    public ObservableBoolean getIsNetworkProblem() {
        return isNetworkProblem;
    }

    public void setIsNetworkProblem(boolean isLoading) {
        this.isNetworkProblem.set(isLoading);
    }
}

