package okuki.sample.mvvm.swapi.list;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import java.util.Arrays;
import java.util.List;

import okuki.sample.mvvm.R;
import okuki.sample.mvvm.common.mvvm.RxObservableField;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.rx2.RxOkuki;
import okuki.sample.mvvm.common.api.swapi.SwapiItem;
import okuki.sample.mvvm.common.mvvm.BaseViewModel;
import okuki.sample.mvvm.common.mvvm.recyclerview.RecyclerItemBinder;
import okuki.sample.mvvm.common.rx.Errors;


public class SwapiListViewModel extends BaseViewModel {

    @Inject
    Okuki okuki;

    @Inject
    SwapiListDataManager swapiListDataManager;

    public final List<SwapiItem.Type> swapiItemTypes = Arrays.asList(SwapiItem.Type.values());
    public final ObservableField<Integer> selectedSwapiItemType = new ObservableField<>();
    public final ObservableBoolean loading = new ObservableBoolean();

    @Override
    public void onAttach() {
        super.onAttach();
        addToAutoDispose(
                RxObservableField.toObservable(selectedSwapiItemType).subscribe(
                        index -> okuki.gotoPlace(new SwapiListPlace(swapiItemTypes.get(index))),
                        Errors.log()
                ),
                RxOkuki.onPlace(okuki, SwapiListPlace.class).subscribe(
                        place -> swapiListDataManager.setSwapiItemType(place.getData()),
                        Errors.log()
                ),
                swapiListDataManager.onLoadingStatus().subscribe(
                        loading::set,
                        Errors.log()
                )
        );
    }

    public ObservableList<SwapiListItemViewModel> itemList(){
        return swapiListDataManager.getItems();
    }

    public RecyclerItemBinder<SwapiListItemViewModel> itemBinder(){
        return new SwapiListItemBinder();
    }

    private class SwapiListItemBinder implements RecyclerItemBinder<SwapiListItemViewModel> {

        @Override
        public int getLayoutRes(SwapiListItemViewModel viewModel) {
            return R.layout.swapi_list_item;
        }

        @Override
        public void onItemBound(int position, SwapiListItemViewModel viewModel) {
            if(isHalfPageLeft(position) && swapiListDataManager.hasMore()){
                swapiListDataManager.loadMore();
            }
        }

        private boolean isHalfPageLeft(int position) {
            return position + (swapiListDataManager.getPageSize() / 2) == swapiListDataManager.getItems().size();
        }

    }

}
