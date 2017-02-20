package okuki.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import okuki.Okuki;
import okuki.sample.common.network.NetworkModule;
import okuki.sample.common.okuki.OkukiStateRestorer;
import okuki.toothpick.PlaceScoper;
import timber.log.Timber;
import toothpick.smoothie.module.SmoothieApplicationModule;

public class App extends Application {

    private static Okuki okuki;
    private static PlaceScoper placeScoper;

    @Override
    public void onCreate() {
        super.onCreate();

        okuki = Okuki.getDefault();
        placeScoper = new PlaceScoper(okuki, new AppModule(), new NetworkModule());

        Timber.plant((BuildConfig.DEBUG) ? new Timber.DebugTree() : new CrashReportingTree());

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            private final OkukiStateRestorer okukiStateRestorer = new OkukiStateRestorer(Okuki.getDefault());

            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                okukiStateRestorer.onRestore(bundle);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                okukiStateRestorer.onSave(bundle);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }

    private class AppModule extends SmoothieApplicationModule {

        AppModule() {
            super(App.this);
            bind(Okuki.class).toInstance(okuki);
        }

    }

    private class CrashReportingTree extends Timber.Tree {

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            //TODO Log to crash reporting

            if (t != null) {
                if (priority == Log.ERROR) {
                    //TODO Log throwable as error to crash reporting
                } else if (priority == Log.WARN) {
                    //TODO Log throwable as warning to crash reporting
                }
            }
        }

    }

    public static void inject(Object obj) {
        try {
            placeScoper.inject(obj);
        } catch (Throwable t) {
            Timber.e(t, "Error injecting %s", obj);
        }
    }

}
