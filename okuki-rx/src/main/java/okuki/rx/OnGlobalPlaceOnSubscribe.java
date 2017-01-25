package okuki.rx;

import java.util.concurrent.atomic.AtomicBoolean;

import okuki.GlobalListener;
import okuki.Okuki;
import okuki.Place;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class OnGlobalPlaceOnSubscribe implements Observable.OnSubscribe<Place> {

    private final Okuki okuki;

    OnGlobalPlaceOnSubscribe(Okuki okuki) {
        this.okuki = okuki;
    }

    @Override
    public void call(final Subscriber<? super Place> subscriber) {
        final GlobalListener listener = new GlobalListener() {
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
                    okuki.removeGlobalListener(listener);
                }
            }

        });

        okuki.addGlobalListener(listener);
    }
}