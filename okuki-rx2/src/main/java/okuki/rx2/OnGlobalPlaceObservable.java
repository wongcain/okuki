package okuki.rx2;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okuki.GlobalListener;
import okuki.Okuki;
import okuki.Place;

class OnGlobalPlaceObservable extends Observable<Place> {

    private final Okuki okuki;

    OnGlobalPlaceObservable(Okuki okuki) {
        this.okuki = okuki;
    }

    @Override
    protected void subscribeActual(Observer<? super Place> observer) {
        Listener listener = new Listener(okuki, observer);
        observer.onSubscribe(listener);
        okuki.addGlobalListener(listener);
    }

    private static class Listener extends GlobalListener implements Disposable {

        private final Okuki okuki;
        private final Observer<? super Place> observer;
        private final AtomicBoolean unsubscribed = new AtomicBoolean();

        private Listener(Okuki okuki, Observer<? super Place> observer) {
            this.okuki = okuki;
            this.observer = observer;
        }

        @Override
        public void onPlace(Place place) {
            if(!isDisposed()){
                try {
                    observer.onNext(place);
                } catch (Exception e) {
                    observer.onError(e);
                    dispose();
                }
            }
        }

        @Override
        public final void dispose() {
            okuki.removeGlobalListener(this);
        }

        @Override
        public boolean isDisposed() {
            return unsubscribed.get();
        }
    }

}
