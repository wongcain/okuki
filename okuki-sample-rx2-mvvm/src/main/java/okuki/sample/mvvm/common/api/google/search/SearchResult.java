package okuki.sample.mvvm.common.api.google.search;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.List;

@AutoValue
public abstract class SearchResult {

    public static JsonAdapter<SearchResult> typeAdapter(final Moshi moshi) {
        return new AutoValue_SearchResult.MoshiJsonAdapter(moshi);
    }

    public abstract List<SearchResultItem> items();

}
