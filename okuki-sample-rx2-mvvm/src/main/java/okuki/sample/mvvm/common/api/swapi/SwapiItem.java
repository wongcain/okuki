package okuki.sample.mvvm.common.api.swapi;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class SwapiItem {

    public enum Type {
        people,
        planets,
        species,
        starships,
        vehicles;
    }

    public static JsonAdapter<SwapiItem> typeAdapter(final Moshi moshi) {
        return new AutoValue_SwapiItem.MoshiJsonAdapter(moshi);
    }

    public abstract String name();

    public abstract String url();


}
