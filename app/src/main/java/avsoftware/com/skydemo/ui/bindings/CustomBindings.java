package avsoftware.com.skydemo.ui.bindings;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxrelay2.Relay;
import com.nextfaze.poweradapters.PowerAdapter;
import com.nextfaze.poweradapters.recyclerview.RecyclerPowerAdapters;

import timber.log.Timber;

/**
 * Created by abennett on 12/03/2018.
 */

public class CustomBindings {

    @BindingAdapter("setMovieAdapter")
    public static void setPowerAdapter(RecyclerView view, PowerAdapter adapter) {
        if (adapter != null)
            view.setAdapter(RecyclerPowerAdapters.toRecyclerAdapter(adapter));
    }

    @BindingAdapter("loadMovieImage")
    public static void setImage(ImageView view, String url) {

        try {
            Glide.with(view.getContext())
                    .applyDefaultRequestOptions(new RequestOptions()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.DATA))
                    .load(url).into(view);
        } catch (Exception e) {
            Timber.e(e, "Failed to load %s with Glide", url);
        }
    }

    @BindingAdapter("connectSearchText")
    public static void connectSearch(SearchView searchView, Relay<String> searchRelay) {
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        //Here we request a search
                        searchRelay.accept(newText);
                        return false;
                    }
                });
    }
}
