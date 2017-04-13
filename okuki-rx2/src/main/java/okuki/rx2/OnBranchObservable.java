package okuki.rx2;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okuki.BranchListener;
import okuki.Okuki;
import okuki.Place;

class OnBranchObservable<B extends Place> extends Observable<Place> {

    private final Okuki okuki;
    private final Class<B> branchClass;

    OnBranchObservable(Okuki okuki, Class<B> branchClass) {
        this.okuki = okuki;
        this.branchClass = branchClass;
    }

    @Override
    protected void subscribeActual(Observer<? super Place> observer) {
        Listener<B> listener = new Listener<>(okuki, branchClass, observer);
        observer.onSubscribe(listener);
        okuki.addBranchListener(listener);
    }

    private static class Listener<B extends Place> extends BranchListener<Place> implements Disposable {

        private final Okuki okuki;
        private final Class<B> branchClass;
        private final Observer<? super Place> observer;
        private final AtomicBoolean unsubscribed = new AtomicBoolean();

        private Listener(Okuki okuki, Class<B> branchClass, Observer<? super Place> observer) {
            this.okuki = okuki;
            this.branchClass = branchClass;
            this.observer = observer;
        }

        @Override
        public Class getBranchClass() {
            return branchClass;
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
            okuki.removeBranchListener(this);
        }

        @Override
        public boolean isDisposed() {
            return unsubscribed.get();
        }
    }

}
