package okuki.android;

import okuki.HistoryAction;
import okuki.Okuki;

public class OkukiParceler {

    private OkukiParceler() {
        throw new AssertionError("No instances.");
    }

    public static OkukiState extract(Okuki okuki) {
        return okuki.getCurrentPlace() == null ? null : new OkukiState(okuki);
    }

    public static void apply(Okuki okuki, OkukiState okukiState) {
        okuki.getHistory().clear();
        for (WrappedPlace wrappedPlace : okukiState.getPlaceHistory()) {
            okuki.getHistory().add(wrappedPlace.getPlace());
        }
        okuki.gotoPlace(okukiState.getCurrentPlace().getPlace(), HistoryAction.NONE);
    }

}
