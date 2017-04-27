package okuki.sample.mvvm.swapi.list;

import android.databinding.ObservableField;
import android.databinding.ObservableList;

import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinder;
import net.droidlabs.mvvm.recyclerview.adapter.binder.ItemBinderBase;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.rx2.RxOkuki;
import okuki.sample.mvvm.BR;
import okuki.sample.mvvm.R;
import okuki.sample.mvvm.common.api.swapi.SwapiItem;
import okuki.sample.mvvm.common.mvvm.BaseViewModel;
import okuki.sample.mvvm.common.rx.Errors;


public class SwapiListViewModel extends BaseViewModel {

    @Inject
    Okuki okuki;

    @Inject
    SwapiListDataManager swapiListDataManager;

    public final ObservableField<String> swapiTypeName = new ObservableField<>();

    @Override
    public void onAttach() {
        super.onAttach();
        addToAutoDispose(
                RxOkuki.onPlace(okuki, SwapiListPlace.class).subscribe(
                        place -> setSwapiItemType(place.getData()),
                        Errors.log()
                )
        );
    }

    public void setSwapiItemType(SwapiItem.Type swapiItemType){
        swapiTypeName.set( swapiItemType.toString() );
        swapiListDataManager.setSwapiItemType( swapiItemType );
    }

    public ItemBinder<SwapiListItemViewModel> itemBinder(){
        return new ItemBinderBase<>( BR.vm, R.layout.swapi_list_item );
    }

    public ObservableList<SwapiListItemViewModel> swapiItemList(){
        return swapiListDataManager.getItems();
    }

}
