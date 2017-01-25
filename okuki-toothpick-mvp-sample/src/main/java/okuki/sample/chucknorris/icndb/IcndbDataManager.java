package okuki.sample.chucknorris.icndb;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okuki.sample.common.network.RetroResults;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IcndbDataManager {


    private final IcndbApi icndbApi;

    public IcndbDataManager(OkHttpClient client, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(IcndbApi.BASE_URL).client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        icndbApi = retrofit.create(IcndbApi.class);

    }

    public Observable<String> getJoke() {
        return icndbApi.getRandomJoke()
                .subscribeOn(Schedulers.io())
                .flatMap(RetroResults.handleResult())
                .map(result -> result.value.joke)
                .observeOn(AndroidSchedulers.mainThread());
    }


}
