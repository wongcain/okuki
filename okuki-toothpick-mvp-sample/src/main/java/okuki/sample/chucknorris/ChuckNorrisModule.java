package okuki.sample.chucknorris;

import com.google.gson.Gson;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okuki.sample.chucknorris.ChuckNorrisPlace;
import okuki.sample.chucknorris.icndb.IcndbDataManager;
import okuki.toothpick.PlaceModule;

public class ChuckNorrisModule extends PlaceModule<ChuckNorrisPlace> {

    @Inject
    OkHttpClient client;

    @Inject
    Gson gson;

    public ChuckNorrisModule() {
        super();
        bind(IcndbDataManager.class).toInstance(new IcndbDataManager(client, gson));
    }

}
