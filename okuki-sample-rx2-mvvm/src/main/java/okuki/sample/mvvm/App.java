package okuki.sample.mvvm;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import okuki.Okuki;
import okuki.android.OkukiParceler;
import okuki.android.OkukiState;
import okuki.rx2.RxOkuki;
import okuki.sample.mvvm.common.lifecycle.RxActivityLifecycleCallbacks;
import okuki.sample.mvvm.common.network.NetworkModule;
import okuki.toothpick.PlaceScoper;
import timber.log.Timber;
import toothpick.smoothie.module.SmoothieApplicationModule;

public class App extends Application {

    private static App APP_INSTANCE;
    private static final String OKUKI_STATE_KEY = OkukiState.class.getName();

    private Okuki okuki;
    private RxActivityLifecycleCallbacks lifecycle;
    private PlaceScoper placeScoper;

    @Override
    public void onCreate() {
        super.onCreate();
        APP_INSTANCE = this;

        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());

        okuki = Okuki.getDefault();
        lifecycle = new RxActivityLifecycleCallbacks(this) {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                if ((okuki.getCurrentPlace() == null) && (bundle != null) && bundle.containsKey(OKUKI_STATE_KEY)) {
                    OkukiState okukiState = bundle.getParcelable(OKUKI_STATE_KEY);
                    if (okukiState != null) {
                        OkukiParceler.apply(okuki, okukiState);
                    }
                }
                super.onActivityCreated(activity, bundle);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                if (bundle != null) {
                    OkukiState okukiState = OkukiParceler.extract(okuki);
                    if (okukiState != null) {
                        bundle.putParcelable(OKUKI_STATE_KEY, okukiState);
                    }
                }
                super.onActivitySaveInstanceState(activity, bundle);
            }

        };
        placeScoper = new PlaceScoper.Builder().okuki(okuki)
                .modules(new AppModule(), new NetworkModule(this)).build();
        RxOkuki.onAnyPlace(okuki).subscribe(place -> Timber.d(place.toString()));
    }

    private class AppModule extends SmoothieApplicationModule {

        AppModule() {
            super(App.this);
            bind(Okuki.class).toInstance(okuki);
            bind(RxActivityLifecycleCallbacks.class).toInstance(lifecycle);
        }

    }

    public static void inject(Object obj) {
        try {
            APP_INSTANCE.placeScoper.inject(obj);
        } catch (Throwable t) {
            Timber.e(t, "Error injecting %s", obj);
        }
    }

    public static <T> T getInstance(Class<T> clazz) {
        return APP_INSTANCE.placeScoper.getInstance(clazz);
    }

}
