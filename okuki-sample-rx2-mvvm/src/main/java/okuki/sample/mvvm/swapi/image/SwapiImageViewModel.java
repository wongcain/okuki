package okuki.sample.mvvm.swapi.image;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.sample.mvvm.common.api.google.search.SearchResultItem;
import okuki.sample.mvvm.common.mvvm.BaseViewModel;


public class SwapiImageViewModel extends BaseViewModel {

    @Inject
    Okuki okuki;

    @Inject
    SwapiImageDataManager swapiImageDataManager;

    public final ObservableField<String> imageUrl = new ObservableField<>();
    public final ObservableField<String> loResImageUrl = new ObservableField<>();
    public final ObservableBoolean hasNext = new ObservableBoolean(true);
    public final ObservableBoolean hasPrev = new ObservableBoolean(false);
    private int index = 0;

    public SwapiImageViewModel(String query) {
        super();
        swapiImageDataManager.setQuery(query);
    }

    @Override
    public void onAttach() {
        super.onAttach();
        addToAutoDispose(
                swapiImageDataManager.onLoadingStatus()
                        .filter(isLoading -> !isLoading && swapiImageDataManager.hasItems())
                        .subscribe(isLoading -> {
                            showImage();
                        })
        );
    }

    private void showImage() {
        if (swapiImageDataManager.numItems() > index) {
            SearchResultItem item = swapiImageDataManager.getItem(index);
            loResImageUrl.set(item.image().thumbnailLink());
            imageUrl.set(item.link());
            hasPrev.set((index > 0));
            hasNext.set(swapiImageDataManager.numItems() > index + 1);
        }
    }

    public void next() {
        index++;
        showImage();
    }

    public void prev() {
        index--;
        showImage();
    }

}
