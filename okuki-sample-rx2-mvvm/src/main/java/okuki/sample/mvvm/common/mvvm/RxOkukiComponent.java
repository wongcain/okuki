package okuki.sample.mvvm.common.mvvm;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okuki.BranchListener;
import okuki.Okuki;
import okuki.Place;

public class RxOkukiComponent {

    private RxOkukiComponent() {
        throw new AssertionError("No instances.");
    }


    public static <BRANCH extends Place> Observable<Component> onComponentBranch( Okuki okuki, Class<BRANCH> branchClass){
        return new ComponentObservable<>(okuki, branchClass);
    }

    static class ComponentObservable<BRANCH extends Place> extends Observable<Component> {

        private final Okuki okuki;
        private final Class<BRANCH> branchClass;

        public ComponentObservable(Okuki okuki, Class<BRANCH> branchClass) {
            this.okuki = okuki;
            this.branchClass = branchClass;
        }

        @Override
        protected void subscribeActual(Observer<? super Component> observer) {
            Listener<BRANCH> listener = new Listener<>(okuki, branchClass, observer);
            observer.onSubscribe(listener);
            okuki.addBranchListener(listener);
        }

        private static class Listener<B extends Place> extends BranchListener<Place> implements Disposable {

            private final Okuki okuki;
            private final Class<B> branchClass;
            private final Observer<? super Component> observer;
            private final AtomicBoolean unsubscribed = new AtomicBoolean();
            private final Component component;

            private Listener(Okuki okuki, Class<B> branchClass, Observer<? super Component> observer) {
                this.okuki = okuki;
                this.branchClass = branchClass;
                this.observer = observer;
                ComponentConfig config = branchClass.getAnnotation(ComponentConfig.class);
                assert config != null;
                component = Component.create(config);
            }

            @Override
            public Class getBranchClass() {
                return branchClass;
            }

            @Override
            public void onPlace(Place place) {
                if(!isDisposed()){
                    try {
                        observer.onNext(component);
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
}
