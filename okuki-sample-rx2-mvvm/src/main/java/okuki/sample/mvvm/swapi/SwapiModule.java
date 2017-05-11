package okuki.sample.mvvm.swapi;

import com.squareup.moshi.Moshi;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okuki.sample.mvvm.common.api.google.search.CustomSearch;
import okuki.sample.mvvm.common.api.swapi.Swapi;
import okuki.sample.mvvm.swapi.list.SwapiListDataManager;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import toothpick.config.Module;

public class SwapiModule extends Module {

    @Inject
    public SwapiModule(OkHttpClient okHttpClient, Moshi moshi) {
        final Retrofit swapiRetrofit = new Retrofit.Builder().baseUrl(Swapi.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
        bind(Swapi.class).toInstance(swapiRetrofit.create(Swapi.class));
        final Retrofit searchRetrofit = new Retrofit.Builder().baseUrl(CustomSearch.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
        bind(CustomSearch.class).toInstance(searchRetrofit.create(CustomSearch.class));
        bind(SwapiListDataManager.class).singletonInScope();
    }

}
