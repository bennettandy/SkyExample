package avsoftware.com.skydemo.ui;

import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.nextfaze.poweradapters.PowerAdapter;
import com.nextfaze.poweradapters.recyclerview.RecyclerPowerAdapters;

import javax.inject.Inject;

import avsoftware.com.skydemo.SkyApplication;
import avsoftware.com.skydemo.databinding.ActivityMainBinding;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

//    private CompositeDisposable mDisposable;

    @Inject
    protected MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mDisposable = new CompositeDisposable();

        SkyApplication.getInstance().component().inject(this);

        ActivityMainBinding binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        binding.setViewModel(mViewModel);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.refreshMovies.accept(true);
    }

    //    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mDisposable != null) {
//            mDisposable.dispose();
//        }
//    }
}
