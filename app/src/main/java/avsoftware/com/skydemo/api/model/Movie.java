package avsoftware.com.skydemo.api.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created by abennett on 12/03/2018.
 *
 * "id": 5123112,
 "title": "Coco",
 "year": "2017",
 "genre": "Animation",
 "poster": "https://image.tmdb.org/t/p/w370_and_h556_bestv2/eKi8dIrr8voobbaGzDpe8w0PVb.jpg"
 */

@AutoValue
public abstract class Movie {
    public abstract int id();
    public abstract String title();
    public abstract String year();
    public abstract String genre();
    public abstract String poster();

    public static TypeAdapter<Movie> typeAdapter(Gson gson) {
        return new AutoValue_Movie.GsonTypeAdapter(gson);
    }
}