package okuki.sample.chucknorris;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okuki.sample.common.api.icndb.IcndbApi;
import okuki.sample.common.api.icndb.IcndbResult;
import okuki.sample.common.network.DataManager;
import okuki.sample.common.network.RetroResults;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class ChuckNorrisDataManager extends DataManager<IcndbResult.Joke> {

    private final IcndbApi icndbApi;

    @Inject
    public ChuckNorrisDataManager(OkHttpClient client, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(IcndbApi.BASE_URL).client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        icndbApi = retrofit.create(IcndbApi.class);
        setPageSize(1);

    }

    @Override
    protected Observable<List<IcndbResult.Joke>> loadData(int limit, int offset) {
        return icndbApi.getRandomJokes(limit)
                .flatMap(RetroResults.handleResult())
                .map(result -> result.jokes);
    }
}
