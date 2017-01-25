package okuki.rx;

import java.util.concurrent.atomic.AtomicBoolean;

import okuki.Okuki;
import okuki.Place;
import okuki.PlaceListener;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class OnPlaceOnSubscribe implements Observable.OnSubscribe<Place> {

    private final Okuki okuki;
    private final Class<? extends Place> placeClass;

    OnPlaceOnSubscribe(Okuki okuki, Class<? extends Place> placeClass) {
        this.okuki = okuki;
        this.placeClass = placeClass;
    }

    @Override
    public void call(final Subscriber<? super Place> subscriber) {
        final PlaceListener listener = new PlaceListener<Place>() {
            @Override
            public Class getPlaceClass() {
                return placeClass;
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
                    okuki.removePlaceListener(listener);
                }
            }

        });

        okuki.addPlaceListener(listener);
    }

}