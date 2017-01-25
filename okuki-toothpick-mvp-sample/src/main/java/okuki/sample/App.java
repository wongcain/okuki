package okuki.sample;

import android.app.Application;
import android.util.Log;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.sample.common.network.NetworkModule;
import okuki.toothpick.PlaceScoper;
import timber.log.Timber;
import toothpick.Toothpick;
import toothpick.smoothie.module.SmoothieApplicationModule;

public class App extends Application {

    @Inject
    Okuki okuki;

    @Override
    public void onCreate() {
        super.onCreate();
        initLogging();
        Toothpick.inject(this, PlaceScoper.openRootScope(new AppModule(this), new NetworkModule()));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void initLogging() {
        Timber.plant((BuildConfig.DEBUG) ? new Timber.DebugTree() : new CrashReportingTree());
    }

    private class AppModule extends SmoothieApplicationModule {

        AppModule(Application app) {
            super(app);
            bind(Okuki.class).toInstance(Okuki.getDefault());
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
}
