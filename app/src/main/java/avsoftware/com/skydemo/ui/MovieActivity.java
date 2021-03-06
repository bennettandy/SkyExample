package avsoftware.com.skydemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;

import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import avsoftware.com.skydemo.R;
import avsoftware.com.skydemo.SkyApplication;
import avsoftware.com.skydemo.databinding.ActivityMovieBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MovieActivity extends AppCompatActivity {

    @Inject
    protected MovieActivityViewModel mViewModel;

    private ActivityMovieBinding mViewBinding;

    private CompositeDisposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDisposable = new CompositeDisposable();

        SkyApplication.getInstance().component().inject(this);

        setUpViewBinding();

        connectObservables();

        setContentView(mViewBinding.getRoot());
    }

    private void setUpViewBinding() {
        mViewBinding = ActivityMovieBinding.inflate(LayoutInflater.from(this));
        int columns = getResources().getInteger(R.integer.display_columns);
        mViewBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, columns));
        mViewBinding.setViewModel(mViewModel);
    }

    private void connectObservables() {
        // connect search box - TODO: ideally move this into custom binding to further de-clutter this class?
        mDisposable.add(RxSearchView.queryTextChanges(mViewBinding.search)
                .debounce(1000, TimeUnit.MILLISECONDS) // FIXME: Losing focus on search view when triggering search, using debounce as workaround
                .map(CharSequence::toString)
                .doOnNext(mViewModel.searchString)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe());

        mDisposable.add(mViewModel.connectObservables());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
