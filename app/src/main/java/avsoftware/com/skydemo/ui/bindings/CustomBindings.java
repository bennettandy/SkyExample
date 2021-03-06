package avsoftware.com.skydemo.ui.bindings;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nextfaze.poweradapters.PowerAdapter;
import com.nextfaze.poweradapters.recyclerview.RecyclerPowerAdapters;

import avsoftware.com.skydemo.R;
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

        Drawable defaultBackground = view.getContext().getResources().getDrawable(R.drawable.broken);

        try {
            Glide.with(view.getContext())
                    .applyDefaultRequestOptions(new RequestOptions()
                            .skipMemoryCache(true)
                            .error(defaultBackground)
                            .diskCacheStrategy(DiskCacheStrategy.DATA))
                    .load(url)
                    .into(view);
        } catch (Exception e) {
            Timber.e(e, "Failed to load %s with Glide", url);
        }
    }
}
