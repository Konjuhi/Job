package com.job.findjob.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.job.findjob.R;
import com.job.findjob.base.BaseActivity;
import com.job.findjob.data.api.ConnectionServer;
import com.job.findjob.data.entity.GithubJob;
import com.job.findjob.data.storage.GithubJobRepository;
import com.job.findjob.databinding.ActivityListBinding;
import com.job.findjob.databinding.ActivityMainBinding;
import com.job.findjob.ui.MainAdapter;
import com.job.findjob.ui.MainMarkedAdapter;
import com.job.findjob.ui.MainViewModel;
import com.job.findjob.ui.detail.DetailActivity;

import javax.inject.Inject;

public class ListActivity extends BaseActivity<ActivityListBinding, MainViewModel> implements MainViewModel.Navigator {

    @Inject
    ConnectionServer server;

    @Inject
    GithubJobRepository repository;

    private ActivityListBinding binding;
    private MainViewModel viewModel;

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_list;
    }

    @Override
    public MainViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel = new ViewModelProvider(this, new MainViewModel.ModelFactory(this, server, repository)
        ).get(MainViewModel.class);
        viewModel.setNavigator(this);
        if(getIntent().getStringExtra("search") != null){
            String keyword = getIntent().getStringExtra("search");
            setUpActionBar("Search Jobs(" + keyword +")");
            viewModel.searchJobFromServer(keyword);
            viewModel.searchLiveData(keyword).observe(this,githubJobs -> {
                if(githubJobs.size() > 0 ){
                    binding.recyclerView.setAdapter(new MainAdapter(githubJobs,viewModel));
                }else{
                    binding.emptyView.setVisibility(View.VISIBLE);
                }

            });
            binding.swipeRefresh.setOnRefreshListener(() ->
                    viewModel.searchJobFromServer(keyword));
        }else{
            Toast.makeText(this,"Failed get result!",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void setUpActionBar(String title){
        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

