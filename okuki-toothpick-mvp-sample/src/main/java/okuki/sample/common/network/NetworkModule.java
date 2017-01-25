package okuki.sample.common.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okuki.sample.BuildConfig;
import toothpick.config.Module;

public class NetworkModule extends Module {

    public NetworkModule() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        bind(OkHttpClient.class).toInstance(client);

        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        bind(Gson.class).toInstance(gson);

    }

}
