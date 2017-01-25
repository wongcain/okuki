package okuki.sample.common.network;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

public final class RetroResults {

    private RetroResults() {
        throw new AssertionError("No instances.");
    }

    public static <T> Func1<Result<T>, Observable<T>> handleResult() {
        return result -> {
            if(result.isError()){
                return Observable.error(result.error());
            } else {
                try {
                    return Observable.just(result.response().body());
                } catch (Throwable t){
                    Timber.e(t, "Error handling result");
                    return Observable.error(t);
                }
            }
        };
    }
}
