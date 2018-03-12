package avsoftware.com.skydemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import avsoftware.com.skydemo.SkyApplication;
import avsoftware.com.skydemo.databinding.ActivityMainBinding;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        mDisposable = new CompositeDisposable();

        SkyApplication.getComponent(this).inject(this);

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
