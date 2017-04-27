package okuki.sample.mvvm.common.network;

import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okuki.sample.mvvm.BuildConfig;
import toothpick.config.Module;

public class NetworkModule extends Module {

    public NetworkModule() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel( BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        bind(OkHttpClient.class).toInstance(client);
        final Moshi moshi = new Moshi.Builder().add( MoshiJsonAdapterFactory.create()).build();
        bind(Moshi.class).toInstance(moshi);
    }

}
