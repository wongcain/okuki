package okuki.rx2;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okuki.Okuki;
import okuki.Place;
import okuki.PlaceListener;

class OnPlaceObservable<P extends Place> extends Observable<P> {

    private final Okuki okuki;
    private final Class<P> placeClass;

    OnPlaceObservable(Okuki okuki, Class<P> placeClass) {
        this.okuki = okuki;
        this.placeClass = placeClass;
    }

    @Override
    protected void subscribeActual(Observer<? super P> observer) {
        Listener<P> listener = new Listener<>(okuki, placeClass, observer);
        observer.onSubscribe(listener);
        okuki.addPlaceListener(listener);
    }

    private static class Listener<P extends Place> extends PlaceListener<Place> implements Disposable {

        private final Okuki okuki;
        private final Class<P> placeClass;
        private final Observer<? super P> observer;
        private final AtomicBoolean unsubscribed = new AtomicBoolean();

        private Listener(Okuki okuki, Class<P> placeClass, Observer<? super P> observer) {
            this.okuki = okuki;
            this.placeClass = placeClass;
            this.observer = observer;
        }

        @Override
        public Class getPlaceClass() {
            return placeClass;
        }

        @Override
        public void onPlace(Place place) {
            if(!isDisposed()){
                try {
                    observer.onNext((P) place);
                } catch (Exception e) {
                    observer.onError(e);
                    dispose();
                }
            }
        }

        @Override
        public final void dispose() {
            okuki.removePlaceListener(this);
        }

        @Override
        public boolean isDisposed() {
            return unsubscribed.get();
        }
    }

}
