package okuki.sample.kittens.detail;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.rx.RxOkuki;
import okuki.sample.common.mvp.PlacePresenter;
import okuki.sample.common.rx.Errors;
import okuki.sample.kittens.KittensDataManager;
import okuki.sample.common.api.giphy.SearchResult;

public class KittensDetailPresenter extends PlacePresenter<KittensDetailPlace, KittensDetailPresenter.Vu> {

    @Inject
    Okuki okuki;

    @Inject
    KittensDataManager dataManager;

    @Override
    protected void onVuAttached() {
        addSubscriptions(
                RxOkuki.onPlace(okuki, KittensDetailPlace.class)
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
