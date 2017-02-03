package okuki.sample.kittens;

import com.google.gson.Gson;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okuki.toothpick.PlaceModule;


public class KittensModule extends PlaceModule<KittensPlace> {

    @Inject
    OkHttpClient client;

    @Inject
    Gson gson;

    public KittensModule() {
        bind(KittensDataManager.class).toInstance(new KittensDataManager(client, gson));
    }
}
