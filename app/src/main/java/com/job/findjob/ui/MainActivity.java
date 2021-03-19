package com.job.findjob.ui;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.job.findjob.R;
import com.job.findjob.base.BaseActivity;
import com.job.findjob.data.api.ConnectionServer;
import com.job.findjob.databinding.ActivityMainBinding;
import com.job.findjob.ui.MainViewModel;


import javax.inject.Inject;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements MainViewModel.Navigator {

    @Inject
    ConnectionServer server;

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel = new ViewModelProvider(this,
                new MainViewModel.ModelFactory(this,server))
                .get(MainViewModel.class);
        viewModel.setNavigator(this);
        viewModel.getJobFromServer();
        viewModel.jobList.observe(this,githubJobs -> {
            binding.recyclerView.setAdapter(new MainAdapter(githubJobs));
        });

    }

    @Override
    public void showProgress() {
        super.showProgress();
    }

    @Override
    public void hideProgressDelay() {
        super.hideProgressDelay();
    }
}