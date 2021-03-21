package com.job.findjob.ui.detail;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.haerul.bottomfluxdialog.BottomFluxDialog;
import com.job.findjob.R;
import com.job.findjob.base.BaseActivity;
import com.job.findjob.base.BaseViewModel;
import com.job.findjob.data.entity.GithubJob;
import com.job.findjob.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity<ActivityDetailBinding, BaseViewModel> {

    private ActivityDetailBinding binding;

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public BaseViewModel getViewModel() {
        return null;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();

        if(getIntent().getSerializableExtra("item") != null){
            GithubJob item = (GithubJob)getIntent().getSerializableExtra("item");
            Log.w(TAG,"onCreate: " + new Gson().toJson(item));
            setUpActionBar(item.title);
            binding.setItem(item);
            binding.description.setText(Html.fromHtml(item.description));
            Glide.with(binding.photoPreview.getContext())
                    .load(item.companyLogo)
                    .error(getResources().getDrawable(R.drawable.ic_round_business_center_24))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            binding.progress.stopShimmer();
                            binding.progress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            binding.progress.stopShimmer();
                            binding.progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(binding.photoPreview);
            binding.btnMark.setOnClickListener(view -> {
                binding.setItem(item);
                Snackbar.make(binding.getRoot(),
                        item.is_mark == 0 ?
                                "\uD83D\uDE13 Unmark " + item.title :
                                "\uD83D\uDE0D Marked " + item.title, Snackbar.LENGTH_SHORT).show();
            });
            binding.btnHowToApply.setOnClickListener(view ->{
                showDialogInfo(item.howToApply);
            });
        }else{
            Toast.makeText(this,"Failed to get job detail!",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showDialogInfo(String howToApply) {
        /*View view = getLayoutInflater().inflate(R.layout.dialog_info,null);
        TextView m = view.findViewById(R.id.message);

        BottomSheetDialog dialog = new BottomSheetDialog(this,R.style.BottomSheetDialogStyle);
        m.setText(howToApply);

        dialog.setContentView(view);
        dialog.show();*/
        BottomFluxDialog.infoDialog(this)
                .setTextTitle("How to apply")
                .setTextMessage(Html.fromHtml(howToApply).toString())
                .setImageDialog(R.drawable.ic_twotone_work_24)
                .setInfoButtonText("CLOSE")
                .show();
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
}
