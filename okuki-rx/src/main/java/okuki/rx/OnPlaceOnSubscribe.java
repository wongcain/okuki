package okuki.rx;

import java.util.concurrent.atomic.AtomicBoolean;

import okuki.Okuki;
import okuki.Place;
import okuki.PlaceListener;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

final class OnPlaceOnSubscribe<P extends Place> implements Observable.OnSubscribe<P> {

    private final Okuki okuki;
    private final Class<P> placeClass;

    OnPlaceOnSubscribe(Okuki okuki, Class<P> placeClass) {
        this.okuki = okuki;
        this.placeClass = placeClass;
    }

    @Override
    public void call(final Subscriber<? super P> subscriber) {
        final PlaceListener listener = new PlaceListener<Place>() {
            @Override
            public Class getPlaceClass() {
                return placeClass;
            }

            @Override
            public void onPlace(Place place) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext((P) place);
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
                    okuki.removePlaceListener(listener);
                }
            }

        });

        okuki.addPlaceListener(listener);
    }

}