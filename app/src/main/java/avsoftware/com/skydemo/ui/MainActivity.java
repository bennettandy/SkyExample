package avsoftware.com.skydemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

        SkyApplication.getComponent(this).inject(this);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setViewModel(mViewModel);

        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null){
            mDisposable.dispose();
        }
    }
}
