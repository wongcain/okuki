package okuki.sample.kittens;

import com.google.gson.Gson;

import java.util.List;

import okhttp3.OkHttpClient;
import okuki.sample.common.api.giphy.GiphyApi;
import okuki.sample.common.api.giphy.SearchResult;
import okuki.sample.common.network.DataManager;
import okuki.sample.common.network.RetroResults;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class KittensDataManager extends DataManager<SearchResult.Giphy> {

    private static final String QUERY = "kittens";
    private final GiphyApi giphyApi;

    public KittensDataManager(OkHttpClient client, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(GiphyApi.BASE_URL).client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        giphyApi = retrofit.create(GiphyApi.class);

    }

    @Override
    protected Observable<List<SearchResult.Giphy>> loadData(int limit, int offset) {
        return giphyApi.search(QUERY, limit, offset)
                .flatMap(RetroResults.handleResult())
                .map(searchResult -> searchResult.getGiphys());
    }

}
