package okuki.sample.mvvm.common.api.google.search;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class SearchResultItemImage {

    public static JsonAdapter<SearchResultItemImage> typeAdapter(final Moshi moshi) {
        return new AutoValue_SearchResultItemImage.MoshiJsonAdapter(moshi);
    }

    public abstract String thumbnailLink();

}
