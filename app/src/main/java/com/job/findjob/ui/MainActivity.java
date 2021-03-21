package com.job.findjob.ui;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.haerul.bottomfluxdialog.BottomFluxDialog;
import com.job.findjob.R;
import com.job.findjob.base.BaseActivity;
import com.job.findjob.data.api.ConnectionServer;
import com.job.findjob.data.entity.GithubJob;
import com.job.findjob.data.storage.GithubJobRepository;
import com.job.findjob.databinding.ActivityMainBinding;
import com.job.findjob.ui.MainViewModel;
import com.job.findjob.ui.detail.DetailActivity;
import com.job.findjob.ui.list.ListActivity;


import javax.inject.Inject;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements MainViewModel.Navigator {

    @Inject
    ConnectionServer server;

    @Inject
    GithubJobRepository repository;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel = new ViewModelProvider(this, new MainViewModel.ModelFactory(this, server, repository)).get(MainViewModel.class);
        viewModel.setNavigator(this);
        viewModel.getJobFromServer();

       /* viewModel.jobList.observe(this, githubJobs -> {
            binding.recyclerView.setAdapter(new MainAdapter(githubJobs));
        });*/

        viewModel.getLiveData().observe(this, githubJobs -> {
            binding.recyclerView.setAdapter(new MainAdapter(githubJobs,viewModel));
        });
        viewModel.getLiveDataMarked().observe(this, githubJobs -> {
            if (githubJobs.size() > 0) {
                binding.markedTitle.setVisibility(View.VISIBLE);
                binding.recyclerViewMarked.setVisibility(View.VISIBLE);
                binding.recyclerViewMarked.setAdapter(new MainMarkedAdapter(githubJobs, viewModel));
            } else {
                binding.markedTitle.setVisibility(View.GONE);
                binding.recyclerViewMarked.setVisibility(View.GONE);
            }
        });
        binding.swipeRefresh.setOnRefreshListener(() -> viewModel.getJobFromServer());
        binding.search.setOnClickListener(view->{
            showDialogSearch();
        });
    }

    private void showDialogSearch() {

        BottomFluxDialog.inputDialog(MainActivity.this)
                .setTextMessage("What are you Looking for?")
                .setRightButtonText("SUBMIT")
                .setInputListener(new BottomFluxDialog.OnInputListener() {
                    @Override
                    public void onSubmitInput(String text) {
                        //Toast.makeText(MainActivity.this,"Input : " + text,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, ListActivity.class);
                        intent.putExtra("search",text);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelInput() {

                    }
                }).show();
    }

    @Override
    public void showProgress() {
        binding.swipeRefresh.setRefreshing(true);
        binding.emptyView.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        binding.recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        binding.swipeRefresh.setRefreshing(false);
        binding.emptyView.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.GONE);
        binding.shimmer.stopShimmer();
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetResult(boolean status, String message) {
        if (!status) { //<-- status result is FALSE
            binding.textEmptyErr.setText(message);
            binding.emptyView.setVisibility(View.VISIBLE);
        } else {
            binding.emptyView.setVisibility(View.GONE);
        }
    }

        @Override
        public void onMark(int mark, String title){
            Snackbar.make(binding.getRoot(), mark == 0 ? "\uD83D\uDE13 Unmark " + title : "\uD83D\uDE0D Marked " + title, Snackbar.LENGTH_SHORT).show();
        }

    @Override
    public void onItemClick(GithubJob githubJob) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("item",githubJob);
        startActivity(intent);

    }
}

