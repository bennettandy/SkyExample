package avsoftware.com.skydemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;

import javax.inject.Inject;

import avsoftware.com.skydemo.SkyApplication;
import avsoftware.com.skydemo.databinding.ActivityMovieBinding;
import io.reactivex.disposables.CompositeDisposable;

public class MovieActivity extends AppCompatActivity {

    @Inject
    protected MovieActivityViewModel mViewModel;

    private CompositeDisposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDisposable = new CompositeDisposable();

        SkyApplication.getInstance().component().inject(this);

        ActivityMovieBinding binding = ActivityMovieBinding.inflate(LayoutInflater.from(this));
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.setViewModel(mViewModel);

        mDisposable.add(mViewModel.searchObservable().subscribe());

        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.tryRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null){
            mDisposable.dispose();
        }
    }
}
