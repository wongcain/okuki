package okuki.sample.mvvm.swapi.list;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okuki.sample.mvvm.common.api.swapi.Page;
import okuki.sample.mvvm.common.api.swapi.Swapi;
import okuki.sample.mvvm.common.api.swapi.SwapiItem;
import okuki.sample.mvvm.common.api.swapi.SwapiItem.Type;
import okuki.sample.mvvm.common.network.DataManager;

public class SwapiListDataManager extends DataManager<SwapiListItemViewModel> {

    @Inject
    Swapi swapi;

    private SwapiItem.Type swapiItemType = null;
    private String nextPageUrl = null;

    @Override
    protected Observable<List<SwapiListItemViewModel>> doLoad(int limitIgnored, int offset) {
        Observable<Page<SwapiItem>> pageObs = (offset == 0)
                ? swapi.getItems(swapiItemType)
                : swapi.getItems(nextPageUrl);
        return pageObs
                .doOnNext(page -> nextPageUrl = page.next())
                .map(page -> {
                    List<SwapiListItemViewModel> list = new ArrayList<>();
                    for (SwapiItem item : page.results()) {
                        list.add(new SwapiListItemViewModel(item));
                    }
                    return list;
                });
    }

    public void setSwapiItemType(@NonNull Type swapiItemType) {
        if (!swapiItemType.equals(this.swapiItemType)) {
            this.swapiItemType = swapiItemType;
            nextPageUrl = null;
            load();
        }
    }

    public boolean hasMore() {
        return !TextUtils.isEmpty(nextPageUrl);
    }

    public Type getSwapiItemType() {
        return swapiItemType;
    }

}
