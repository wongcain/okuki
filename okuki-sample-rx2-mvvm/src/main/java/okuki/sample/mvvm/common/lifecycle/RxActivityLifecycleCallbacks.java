package okuki.sample.mvvm.common.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.jakewharton.rxrelay2.PublishRelay;

import io.reactivex.Observable;

public class RxActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    public enum Event {
        CREATE,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY
    }

    private final PublishRelay<Event> mLifecycleRelay = PublishRelay.create();
    private final PublishRelay<Bundle> mSaveStateRelay = PublishRelay.create();
    private final PublishRelay<Bundle> mLoadStateRelay = PublishRelay.create();

    public RxActivityLifecycleCallbacks(Context context) {
        Application app = (Application) context.getApplicationContext();
        app.registerActivityLifecycleCallbacks(this);
    }

    private void lifecycleEvent(RxActivityLifecycleCallbacks.Event event) {
        mLifecycleRelay.accept(event);
    }

    private void saveState(Bundle bundle) {
        if (bundle != null) {
            mSaveStateRelay.accept(bundle);
        }
    }

    private void loadState(Bundle bundle) {
        if (bundle != null) {
            mLoadStateRelay.accept(bundle);
        }
    }

    public Observable<Event> onLifeCycleEvent() {
        return mLifecycleRelay;
    }

    public Observable<Bundle> onLoadState() {
        return mLoadStateRelay;
    }

    public Observable<Bundle> onSaveState() {
        return mSaveStateRelay;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        loadState(bundle);
        lifecycleEvent(Event.CREATE);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        lifecycleEvent(Event.START);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        lifecycleEvent(Event.RESUME);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        lifecycleEvent(Event.PAUSE);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        lifecycleEvent(Event.STOP);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        saveState(bundle);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        lifecycleEvent(Event.DESTROY);
    }

}
