package okuki.sample.mvvm.swapi.list;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.sample.mvvm.common.api.swapi.SwapiItem;
import okuki.sample.mvvm.common.mvvm.BaseViewModel;
import okuki.sample.mvvm.common.rx.Errors;
import okuki.sample.mvvm.common.rx.RxObservableField;


public class SwapiListViewModel extends BaseViewModel {

    @Inject
    Okuki okuki;

    @Inject
    SwapiListDataManager swapiListDataManager;

    public final List<SwapiItem.Type> itemTypes = Arrays.asList(SwapiItem.Type.values());

    public final ObservableField<Integer> selectedItemTypeIndex = new ObservableField<>();

    public final ObservableBoolean loading = new ObservableBoolean();

    public final SwapiListItemBinding itemBinding = new SwapiListItemBinding();

    public ObservableList<SwapiListItemViewModel> items() {
        return swapiListDataManager.getItems();
    }

    public void onItemBound(int position) {
        if(isHalfPageLeft(position)) swapiListDataManager.loadMore();
    }

    private boolean isHalfPageLeft(int position) {
        return position + (swapiListDataManager.getPageSize() / 2) == swapiListDataManager.getItems().size();
    }

    @Override
    public void onAttach() {
        addToAutoDispose(
                RxObservableField.toObservable(selectedItemTypeIndex)
                        .map(itemTypes::get)
                        .subscribe(
                                this::onItemTypeChanged,
                                Errors.log()
                        ),
                swapiListDataManager.onLoadingStatus().subscribe(
                        loading::set,
                        Errors.log()
                )
        );
    }


    /* Because of 2-way binding on selectedItemTypeIndex, we need to wait until the value is set via
       the initial binding before we set the value from the saved state in swapiListDataManager.
     */
    private AtomicBoolean isInitialized = new AtomicBoolean();

    private void onItemTypeChanged(SwapiItem.Type newItemType) {
        if (isInitialized.getAndSet(true) || swapiListDataManager.getSwapiItemType() == null) {
            swapiListDataManager.setSwapiItemType(newItemType);
        } else {
            selectedItemTypeIndex.set(itemTypes.indexOf(swapiListDataManager.getSwapiItemType()));
        }
    }

}
