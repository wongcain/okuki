package okuki.sample.chucknorris;

import com.google.gson.Gson;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okuki.toothpick.PlaceModule;

public class ChuckNorrisModule extends PlaceModule<ChuckNorrisPlace> {

    @Inject
    OkHttpClient client;

    @Inject
    Gson gson;

    public ChuckNorrisModule() {
        super();
        ChuckNorrisDataManager dm = new ChuckNorrisDataManager(client, gson);
        dm.setPageSize(1);
        bind(ChuckNorrisDataManager.class).toInstance(dm);
    }

}
