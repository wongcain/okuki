package okuki.sample.kittens;

import com.google.gson.Gson;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okuki.sample.common.lifecycle.RxActivityLifecycleCallbacks;
import toothpick.config.Module;


public class KittensModule extends Module {

    @Inject
    public KittensModule(OkHttpClient client, Gson gson, RxActivityLifecycleCallbacks lifecycle) {
        bind(KittensDataManager.class).toInstance(new KittensDataManager(client, gson, lifecycle));
    }

}
