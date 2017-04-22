package okuki.sample.mvvm.common.network;

import com.ryanharter.auto.value.moshi.MoshiAdapterFactory;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okuki.sample.mvvm.BuildConfig;
import okuki.sample.mvvm.common.api.swapi.AutoValueMoshi_MyAdapterFactory;
import toothpick.config.Module;

public class NetworkModule extends Module {

    public NetworkModule() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel( BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        bind(OkHttpClient.class).toInstance(client);


        final Moshi moshi = new Moshi.Builder().add( MyAdapterFactory.create()).build();
        bind(Moshi.class).toInstance(moshi);
    }

    @MoshiAdapterFactory
    public abstract static class MyAdapterFactory implements JsonAdapter.Factory {

        public static JsonAdapter.Factory create() {
            return new AutoValueMoshi_MyAdapterFactory();
        }

    }
}
