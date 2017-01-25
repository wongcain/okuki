package okuki.sample.common.rx;

import rx.functions.Func1;

public class StateChangeHelper {
    private Object lastState = null;

    public Func1<Object, Boolean> onlyNewFilter(Object state) {
        return ignore -> {
            boolean isNew = !state.equals(lastState);
            if (isNew) {
                lastState = state;
            }
            return isNew;
        };
    }
}
