package okuki.sample.kittens;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.rx.RxOkuki;
import okuki.sample.common.mvp.PlacePresenter;
import okuki.sample.common.rx.Errors;
import okuki.sample.kittens.detail.KittensDetailPlace;
import okuki.sample.kittens.giphy.GiphyDataManager;
import okuki.sample.kittens.giphy.SearchResult;
import timber.log.Timber;
import toothpick.config.Module;

class KittensPresenter extends PlacePresenter<KittensPlace, KittensPresenter.Vu> {

    private static final int PAGE_SIZE = 20;

    @Inject
    Okuki okuki;

    @Inject
    GiphyDataManager giphyDataManager;

    @Inject
    KittensResultsList kittensResultsList;

    @Override
    protected void onVuAttached() {
        super.onVuAttached();
        addSubscriptions(
                RxOkuki.onBranch(okuki, KittensDetailPlace.class).subscribe(
                        place -> {
                            Timber.d("test");
                            getVu().loadKittensDetails();
                        },
                        Errors.log()
                ),
                giphyDataManager.search("kittens", PAGE_SIZE, 0).subscribe(results -> {
                    kittensResultsList.clear();
                    kittensResultsList.addAll(results);
                    getVu().notifyUpdated();
                }, Errors.log())
        );
    }

    @Override
    protected void onDetachVu() {
        super.onDetachVu();
    }

    int getNumKittens() {
        return kittensResultsList.size();
    }

    String getKittenImgUrl(int index) {
        if ((index % (PAGE_SIZE / 2) == 0) && (index + PAGE_SIZE > kittensResultsList.size())) {
            loadMoreKittens();
        }
        return kittensResultsList.get(index).getImage(SearchResult.Giphy.ImageType.fixed_width_small).getUrl();
    }

    void loadMoreKittens() {
        addSubscriptions(
                giphyDataManager.search("kittens", PAGE_SIZE, kittensResultsList.size()).subscribe(results -> {
                    int pos = kittensResultsList.size();
                    kittensResultsList.addAll(results);
                    getVu().notifyInserted(pos, results.size());
                }, Errors.log())
        );
    }

    void handleKittenSelected(int index) {
        okuki.gotoPlace(new KittensDetailPlace(index));
    }

    @NonNull
    @Override
    protected List<Module> getModules() {
        return Collections.singletonList(new KittensModule());
    }

    interface Vu {

        void notifyUpdated();

        void notifyInserted(int position, int count);

        void loadKittensDetails();

    }

}
