package okuki.sample.mvvm.common.api.swapi;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.lang.reflect.Type;
import java.util.List;

@AutoValue
public abstract class Page<T> {

    public static <T> JsonAdapter<Page<T>> typeAdapter( final Moshi moshi, final Type[] types) {
        return new AutoValue_Page.MoshiJsonAdapter<>(moshi, types);
    }

    @Nullable
    abstract Integer count();

    @Nullable
    abstract String next();

    @Nullable
    abstract String previous();

    abstract List<T> results();

}
