package okuki.sample.mvvm.swapi;

import com.squareup.moshi.Moshi;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okuki.sample.mvvm.common.api.swapi.Swapi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import toothpick.config.Module;

public class SwapiModule extends Module {

    @Inject
    public SwapiModule(OkHttpClient okHttpClient, Moshi moshi) {
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(Swapi.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
        bind(Swapi.class).toInstance(retrofit.create(Swapi.class));
    }

}
