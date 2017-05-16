package okuki.sample.mvvm.swapi.image;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okuki.sample.mvvm.common.api.google.search.CustomSearch;
import okuki.sample.mvvm.common.api.google.search.SearchResultItem;
import okuki.sample.mvvm.common.network.DataManager;

public class SwapiImageDataManager extends DataManager<SearchResultItem> {

    @Inject
    CustomSearch customSearch;

    private String query;

    public void setQuery(String query) {
        this.query = query;
        load();
    }

    @Override
    protected Observable<List<SearchResultItem>> doLoad(int limitIgnored, int offsetIgnored) {
        return customSearch.search(CustomSearch.CX, CustomSearch.API_KEY,
                CustomSearch.SearchType.image, query).map(searchResult -> searchResult.items());
    }

}
