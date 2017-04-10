package okuki.rx;

import java.util.concurrent.atomic.AtomicBoolean;

import okuki.BranchListener;
import okuki.Okuki;
import okuki.Place;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

final class OnBranchOnSubscribe implements Observable.OnSubscribe<Place> {

    private final Okuki okuki;
    private final Class<? extends Place> branchClass;

    OnBranchOnSubscribe(Okuki okuki, Class<? extends Place> branchClass) {
        this.okuki = okuki;
        this.branchClass = branchClass;
    }

    @Override
    public void call(final Subscriber<? super Place> subscriber) {
        final BranchListener listener = new BranchListener<Place>() {
            @Override
            public Class getBranchClass() {
                return branchClass;
            }

            @Override
            public void onPlace(Place place) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(place);
                }
            }

            @Override
            public void onError(Throwable error) {
                subscriber.onError(error);
            }
        };

        subscriber.add(new Subscription() {

            private final AtomicBoolean unsubscribed = new AtomicBoolean();

            @Override
            public final boolean isUnsubscribed() {
                return unsubscribed.get();
            }

            @Override
            public void unsubscribe() {
                if (unsubscribed.compareAndSet(false, true)) {
                    okuki.removeBranchListener(listener);
                }
            }

        });

        okuki.addBranchListener(listener);
    }

}