package okuki.sample.kittens.giphy;

import com.google.gson.Gson;

import java.util.List;

import okhttp3.OkHttpClient;
import okuki.sample.common.network.RetroResults;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GiphyDataManager {


    private final GiphyApi giphyApi;

    public GiphyDataManager(OkHttpClient client, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(GiphyApi.BASE_URL).client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        giphyApi = retrofit.create(GiphyApi.class);

    }

    public Observable<List<SearchResult.Giphy>> search(String query, int limit, int offset) {
        return giphyApi.search(query, limit, offset)
                .subscribeOn(Schedulers.io())
                .flatMap(RetroResults.handleResult())
                .map(searchResult -> searchResult.getGiphys())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
