package com.job.findjob.data.storage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.job.findjob.data.entity.GithubJob;

import java.util.List;

@Dao
public interface GithubJobDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GithubJob githubJob);

    @Query("SELECT * FROM githubjob ORDER BY createdAt DESC LIMIT 10")
    LiveData<List<GithubJob>> getLiveData();

    @Query("SELECT * FROM githubjob where is_mark = 1 ORDER BY createdAt DESC")
    LiveData<List<GithubJob>> getLiveDataMarked();

    @Query("SELECT * FROM githubjob where id = :id")
    GithubJob getDataById(String id);

    @Query("SELECT * FROM githubjob WHERE LOWER(title) LIKE :keyword OR LOWER(description) LIKE :keyword ORDER BY createdAt DESC")
    LiveData<List<GithubJob>> searchLiveData(String keyword);


}
