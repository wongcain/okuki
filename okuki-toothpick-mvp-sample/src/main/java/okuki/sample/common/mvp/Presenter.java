package okuki.sample.common.mvp;

import okuki.sample.App;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class Presenter<V> {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private V vu;

    public final void attachVu(V vu) {
        this.vu = vu;
        App.inject(this);
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

}
