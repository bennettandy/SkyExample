package avsoftware.com.skydemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;

import javax.inject.Inject;

import avsoftware.com.skydemo.SkyApplication;
import avsoftware.com.skydemo.databinding.ActivityMovieBinding;

public class MovieActivity extends AppCompatActivity {

    @Inject
    protected MovieActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SkyApplication.getInstance().component().inject(this);

        ActivityMovieBinding binding = ActivityMovieBinding.inflate(LayoutInflater.from(this));
        binding.setViewModel(mViewModel);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mViewModel.tryRefresh();

        setContentView(binding.getRoot());
    }
}
