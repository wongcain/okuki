package okuki.sample.mvvm.common.api.google.search;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;


@AutoValue
public abstract class SearchResultItem {

    public static JsonAdapter<SearchResultItem> typeAdapter(final Moshi moshi) {
        return new AutoValue_SearchResultItem.MoshiJsonAdapter(moshi);
    }

    public abstract SearchResultItemImage image();

}
