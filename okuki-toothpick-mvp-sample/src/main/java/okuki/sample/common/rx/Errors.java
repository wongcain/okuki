package okuki.sample.common.rx;

import rx.functions.Action1;
import timber.log.Timber;

public final class Errors {

    private Errors() {
        throw new AssertionError("No instances.");
    }

    private static final Action1<Throwable> LOG_ACTION = throwable -> {
        Timber.e(throwable, "Error in subscription");
    };

    public static Action1<Throwable> log(){
        return LOG_ACTION;
    }

}
