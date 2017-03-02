package okuki.sample.kittens.detail;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.rx.RxOkuki;
import okuki.sample.common.api.giphy.SearchResult;
import okuki.sample.common.mvp.Presenter;
import okuki.sample.common.rx.Errors;
import okuki.sample.kittens.KittensDataManager;
import timber.log.Timber;

public class KittensDetailPresenter extends Presenter<KittensDetailPresenter.Vu> {

    @Inject
    Okuki okuki;

    @Inject
    KittensDataManager dataManager;

    @Override
    protected void onVuAttached() {
        addSubscriptions(
                RxOkuki.onAnyPlace(okuki).subscribe(place -> Timber.d("PLACE EVENT: " + place), Errors.log()),
                RxOkuki.onPlace(okuki, KittensDetailPlace.class)
                        .doOnNext(place -> Timber.d(place.toString()))
                        .map(place -> place.getData())
                        .filter(index -> index < dataManager.getNumResults())
                        .subscribe(this::loadKittenImg, Errors.log())
        );
    }

    private void loadKittenImg(int index) {
        getVu().loadImage(dataManager.getResult(index).getImage(SearchResult.Giphy.ImageType.fixed_height).getUrl());
    }

    boolean handleBack() {
        return okuki.goBack();
    }

    interface Vu {

        void loadImage(String url);

    }

}
