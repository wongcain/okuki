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

    @SuppressWarnings("unchecked")
    public static <T extends Place> Observable<T> onPlace(Okuki okuki, Class<T> clazz) {
        return Observable.create(new OnPlaceOnSubscribe(okuki, clazz));
    }

    public static Observable<Place> onBranch(Okuki okuki, Class<? extends Place> clazz) {
        return Observable.create(new OnBranchOnSubscribe(okuki, clazz));
    }

}
