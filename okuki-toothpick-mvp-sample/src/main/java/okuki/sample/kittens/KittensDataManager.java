package okuki.sample.kittens;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.OkHttpClient;
import okuki.sample.common.api.giphy.GiphyApi;
import okuki.sample.common.api.giphy.SearchResult;
import okuki.sample.common.lifecycle.RxActivityLifecycleCallbacks;
import okuki.sample.common.network.DataManager;
import okuki.sample.common.network.RetroResults;
import okuki.sample.common.rx.Errors;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class KittensDataManager extends DataManager<SearchResult.Giphy> {

    private static final String QUERY = "kittens";
    private static final String KEY = KittensDataManager.class.getName();
    private static final Type LIST_TYPE = new ListTypeToken().getType();
    private final GiphyApi giphyApi;

    public KittensDataManager(OkHttpClient client, Gson gson, RxActivityLifecycleCallbacks lifecycle) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(GiphyApi.BASE_URL).client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        giphyApi = retrofit.create(GiphyApi.class);
        lifecycle.onSaveState()
                .filter(bundle -> bundle != null)
                .subscribe(
                        bundle -> bundle.putString(KEY, gson.toJson(getResults())),
                        Errors.log()
                );
        lifecycle.onLoadState()
                .filter(bundle -> bundle != null && bundle.containsKey(KEY))
                .subscribe(
                        bundle -> getResults().addAll(gson.fromJson(bundle.getString(KEY), LIST_TYPE)),
                        Errors.log()
                );


    }

    @Override
    protected Observable<List<SearchResult.Giphy>> loadData(int limit, int offset) {
        return giphyApi.search(QUERY, limit, offset)
                .flatMap(RetroResults.handleResult())
                .map(searchResult -> searchResult.getGiphys());
    }

    private static class ListTypeToken extends TypeToken<List<SearchResult.Giphy>> {
    }

}
