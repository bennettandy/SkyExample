package avsoftware.com.skydemo.api.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

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
public abstract class MovieResponse {

    public abstract List<Movie> data();

    public static TypeAdapter<MovieResponse> typeAdapter(Gson gson) {
        return new AutoValue_MovieResponse.GsonTypeAdapter(gson);
    }
}