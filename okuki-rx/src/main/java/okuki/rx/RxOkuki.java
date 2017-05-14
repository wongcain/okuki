package okuki.rx;

import okuki.Okuki;
import okuki.Place;
import rx.Observable;

public class RxOkuki {

    private RxOkuki() {
        throw new AssertionError("No instances.");
    }

    public static Observable<Place> onAnyPlace(Okuki okuki) {
        return Observable.create(new OnGlobalPlaceOnSubscribe(okuki));
    }

    public static <P extends Place> Observable<P> onPlace(Okuki okuki, Class<P> clazz) {
        return Observable.create(new OnPlaceOnSubscribe<>(okuki, clazz));
    }

    public static <B extends Place> Observable<Place> onBranch(Okuki okuki, Class<B> clazz) {
        return Observable.create(new OnBranchOnSubscribe<>(okuki, clazz));
    }

}
