package com.job.findjob.di;

import com.job.findjob.ui.MainActivity;
import com.job.findjob.ui.detail.DetailActivity;
import com.job.findjob.ui.list.ListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector
    abstract DetailActivity detailActivity();

    @ContributesAndroidInjector
    abstract ListActivity listActivity();

}
