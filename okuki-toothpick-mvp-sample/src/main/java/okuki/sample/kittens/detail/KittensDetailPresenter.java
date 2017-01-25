package okuki.sample.kittens.detail;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.rx.RxOkuki;
import okuki.sample.common.mvp.PlacePresenter;
import okuki.sample.common.rx.Errors;
import okuki.sample.kittens.KittensResultsList;
import okuki.sample.kittens.giphy.SearchResult;

public class KittensDetailPresenter extends PlacePresenter<KittensDetailPlace, KittensDetailPresenter.Vu> {

    @Inject
    Okuki okuki;

    @Inject
    KittensResultsList kittensResultsList;

    @Override
    protected void onVuAttached() {
        addSubscriptions(
                RxOkuki.onPlace(okuki, KittensDetailPlace.class).subscribe(
                        place -> loadKittenImg(((KittensDetailPlace) place).getData()),
                        Errors.log()
                )
        );
    }

    private void loadKittenImg(int index) {
        getVu().loadImage(kittensResultsList.get(index).getImage(SearchResult.Giphy.ImageType.fixed_height).getUrl());
    }

    boolean handleBack() {
        return okuki.goBack();
    }

    interface Vu {

        void loadImage(String url);

    }

}
