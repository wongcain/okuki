package okuki.rx2;

import io.reactivex.Observable;
import okuki.Okuki;
import okuki.Place;

public class RxOkuki {

    private RxOkuki() {
        throw new AssertionError("No instances.");
    }

    public static Observable<Place> onAnyPlace(Okuki okuki) {
        return new OnGlobalPlaceObservable(okuki);
    }

    public static <P extends Place> Observable<P> onPlace(Okuki okuki, Class<P> clazz) {
        return new OnPlaceObservable<>(okuki, clazz);
    }

    public static <B extends Place> Observable<Place> onBranch(Okuki okuki, Class<B> clazz) {
        return new OnBranchObservable<>(okuki, clazz);
    }

}
