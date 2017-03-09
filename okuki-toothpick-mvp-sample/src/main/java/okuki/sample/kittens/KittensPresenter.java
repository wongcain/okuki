package okuki.sample.kittens;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.rx.RxOkuki;
import okuki.sample.common.api.giphy.SearchResult;
import okuki.sample.common.mvp.Presenter;
import okuki.sample.common.rx.Errors;
import okuki.sample.kittens.detail.KittensDetailPlace;

class KittensPresenter extends Presenter<KittensPresenter.Vu> {

    @Inject
    Okuki okuki;

    @Inject
    KittensDataManager dataManager;

    @Override
    protected void onVuAttached() {
        super.onVuAttached();
        addSubscriptions(
                RxOkuki.onBranch(okuki, KittensDetailPlace.class).subscribe(
                        place -> {
                            getVu().loadKittensDetails();
                        },
                        Errors.log()
                ),
                dataManager.onListUpdated().subscribe(
                        aVoid -> getVu().notifyUpdated(),
                        Errors.log()
                ),
                dataManager.onRangeInserted().subscribe(
                        range -> getVu().notifyInserted(range.getLower(), range.getUpper()),
                        Errors.log()
                )
        );
        if (dataManager.getNumResults() == 0) {
            dataManager.load();
        }
    }

    @Override
    protected void onDetachVu() {
        super.onDetachVu();
    }

    int getNumItems() {
        return dataManager.getNumResults();
    }

    String getItemImage(int index) {
        if (isHalfPageLeft(index)) {
            dataManager.loadMore();
        }
        return dataManager.getResult(index).getImage(SearchResult.Giphy.ImageType.fixed_width_small).getUrl();
    }

    private boolean isHalfPageLeft(int index) {
        return index + (dataManager.getPageSize() / 2) == getNumItems();
    }

    void handleItemSelected(int index) {
        okuki.gotoPlace(new KittensDetailPlace(index));
    }

    interface Vu {

        void notifyUpdated();

        void notifyInserted(int position, int count);

        void loadKittensDetails();

    }

}
