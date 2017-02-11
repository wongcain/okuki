package okuki.sample.main;

import java.util.Date;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.rx.RxOkuki;
import okuki.sample.R;
import okuki.sample.chucknorris.ChuckNorrisPlace;
import okuki.sample.common.mvp.Presenter;
import okuki.sample.common.rx.Errors;
import okuki.sample.common.rx.StateChangeHelper;
import okuki.sample.kittens.KittensPlace;
import okuki.sample.welcome.WelcomePlace;
import rx.Observable;
import timber.log.Timber;

public class MainPresenter extends Presenter<MainPresenter.Vu> {

    @Inject
    Okuki okuki;

    @Override
    protected void onVuAttached() {
        StateChangeHelper stateChangeHelper = new StateChangeHelper();
        addSubscriptions(
                RxOkuki.onAnyPlace(okuki).subscribe(place -> Timber.d("PLACE EVENT: " + place), Errors.log()),

                // TODO consolidate filter/doOnNext somehow
                RxOkuki.onBranch(okuki, WelcomePlace.class)
                        .filter(stateChangeHelper.onlyNewFilter(WelcomePlace.class))
                        .subscribe(place -> getVu().loadWelcome(), Errors.log()),

                RxOkuki.onBranch(okuki, ChuckNorrisPlace.class)
                        .filter(stateChangeHelper.onlyNewFilter(ChuckNorrisPlace.class))
                        .subscribe(place -> getVu().loadCheckNorris(), Errors.log()),

                RxOkuki.onBranch(okuki, KittensPlace.class)
                        .filter(stateChangeHelper.onlyNewFilter(KittensPlace.class))
                        .subscribe(place -> getVu().loadKittens(), Errors.log()),

                getVu().onMenuSelection().subscribe(
                        itemId -> {
                            switch (itemId) {
                                case R.id.action_kittens:
                                    okuki.gotoPlace(new KittensPlace());
                                    break;
                                case R.id.action_chuck_norris:
                                    okuki.gotoPlace(new ChuckNorrisPlace());
                                    break;
                            }
                        },
                        Errors.log()
                ),

                getVu().onNavHomeClick().subscribe(aVoid -> handleBack(), Errors.log())
        );
        if (okuki.getCurrentPlace() == null) okuki.gotoPlace(new WelcomePlace(new Date()));
    }

    boolean handleBack() {
        return okuki.goBack();
    }

    interface Vu {

        Observable<Integer> onMenuSelection();

        Observable<Void> onNavHomeClick();

        void loadWelcome();

        void loadCheckNorris();

        void loadKittens();

    }

}
