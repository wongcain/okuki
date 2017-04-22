package okuki.sample.mvvm.common.rx;

import io.reactivex.functions.Consumer;
import timber.log.Timber;

public final class Errors {

    private Errors() {
        throw new AssertionError("No instances.");
    }

    private static final Consumer<Throwable> LOG_ACTION = throwable -> Timber.e(throwable, "Error in subscription");

    private static final Consumer<Throwable> THROW_ACTION = throwable -> {
        throw new RuntimeException( throwable );
    };

    public static Consumer<Throwable> log(){
        return LOG_ACTION;
    }

    public static Consumer<Throwable> rethrow() {
        return THROW_ACTION;
    }

}
