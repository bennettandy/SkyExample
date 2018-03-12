package avsoftware.com.skydemo.api.model;

/**
 * Created by abennett on 12/03/2018.
 */

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

@GsonTypeAdapterFactory
public abstract class GeneratedTypeAdapterFactory implements TypeAdapterFactory {
    // Static factory method to access the package
    // private generated implementation
    public static TypeAdapterFactory create() {
        return  new AutoValueGson_GeneratedTypeAdapterFactory();
    }
}
