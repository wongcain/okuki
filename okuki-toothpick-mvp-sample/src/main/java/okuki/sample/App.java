package okuki.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.sample.common.network.NetworkModule;
import okuki.sample.common.okuki.OkukiStateRestorer;
import okuki.toothpick.PlaceScoper;
import timber.log.Timber;
import toothpick.Toothpick;
import toothpick.smoothie.module.SmoothieApplicationModule;

public class App extends Application {

    @Inject
    Okuki okuki;

    @Inject
    OkukiStateRestorer mOkukiStateRestorer;

    @Override
    public void onCreate() {
        super.onCreate();
        initLogging();
        injectThis();
        initOkukiSaveState();
    }

    private void initLogging() {
        Timber.plant((BuildConfig.DEBUG) ? new Timber.DebugTree() : new CrashReportingTree());
    }

    private void injectThis() {
        Toothpick.inject(this, PlaceScoper.openRootScope(new AppModule(this), new NetworkModule()));
    }

    private void initOkukiSaveState() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                mOkukiStateRestorer.onRestore(bundle);
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
                mOkukiStateRestorer.onSave(bundle);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private class AppModule extends SmoothieApplicationModule {

        AppModule(Application app) {
            super(app);
            bind(Okuki.class).toInstance(Okuki.getDefault());
            bind(OkukiStateRestorer.class).singletonInScope();
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
