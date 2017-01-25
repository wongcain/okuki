package okuki.sample.kittens;

import com.google.gson.Gson;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okuki.sample.kittens.giphy.GiphyDataManager;
import okuki.toothpick.PlaceModule;


public class KittensModule extends PlaceModule<KittensPlace> {

    @Inject
    OkHttpClient client;

    @Inject
    Gson gson;

    public KittensModule() {
        bind(GiphyDataManager.class).toInstance(new GiphyDataManager(client, gson));
        bind(KittensResultsList.class).singletonInScope();
    }
}
