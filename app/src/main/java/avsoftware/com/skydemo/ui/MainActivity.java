package avsoftware.com.skydemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import javax.inject.Inject;

import avsoftware.com.skydemo.SkyApplication;
import avsoftware.com.skydemo.databinding.ActivityMainBinding;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable mDisposable;

    @Inject
    protected MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDisposable = new CompositeDisposable();

        SkyApplication.getInstance().component().inject(this);

        ActivityMainBinding binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        binding.setViewModel(mViewModel);

        mDisposable.add(
                mViewModel.connect().subscribe()
        );

        setContentView(binding.getRoot());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
