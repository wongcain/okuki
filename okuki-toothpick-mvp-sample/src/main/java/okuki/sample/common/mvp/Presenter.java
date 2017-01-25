package okuki.sample.common.mvp;

import android.support.annotation.NonNull;

import okuki.toothpick.PlaceScoper;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;
import toothpick.Scope;
import toothpick.Toothpick;

public abstract class Presenter<V> {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private Scope scope;
    private V vu;

    public Presenter() {
        scope = PlaceScoper.openRootScope();
    }

    public Presenter(@NonNull Scope scope) {
        this.scope = scope;
    }

    public final void attachVu(V vu) {
        this.vu = vu;
        try {
            Toothpick.inject(this, scope);
        } catch (Exception e) {
            Timber.e(e, "Failed to inject self");
        }
        onVuAttached();
    }

    protected void onVuAttached() {
    }

    public final void detachVu() {
        onDetachVu();
        compositeSubscription.clear();
        vu = null;
    }

    protected void onDetachVu() {
    }

    protected V getVu() {
        return vu;
    }

    protected void addSubscriptions(Subscription... subscriptions) {
        compositeSubscription.addAll(subscriptions);
    }

    /**
     * Manually removes an Rx subscription from the auto-unsubscribe collection
     */
    protected void removeSubscription(Subscription subscription) {
        compositeSubscription.remove(subscription);
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

}
