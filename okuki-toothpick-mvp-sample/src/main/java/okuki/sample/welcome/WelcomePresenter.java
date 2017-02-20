package okuki.sample.welcome;

import java.util.Date;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.rx.RxOkuki;
import okuki.sample.common.mvp.Presenter;
import okuki.sample.common.rx.Errors;

public class WelcomePresenter extends Presenter<WelcomePresenter.Vu> {

    @Inject
    Okuki okuki;

    @Override
    protected void onVuAttached() {
        addSubscriptions(
                RxOkuki.onPlace(okuki, WelcomePlace.class).subscribe(
                        place -> getVu().setStartedTime(place.getData()),
                        Errors.log()
                )
        );
    }

    interface Vu {
        void setStartedTime(Date startedTime);
    }

}
