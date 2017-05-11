package okuki.sample.mvvm.swapi.list;

import android.databinding.ObservableField;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.sample.mvvm.common.api.swapi.SwapiItem;
import okuki.sample.mvvm.common.mvvm.BaseViewModel;
import okuki.sample.mvvm.swapi.image.SwapiImagePlace;

public class SwapiListItemViewModel extends BaseViewModel {

    @Inject
    Okuki okuki;

    public final ObservableField<String> name = new ObservableField<>();

    public SwapiListItemViewModel(SwapiItem swapiItem) {
        name.set(swapiItem.name());
    }

    public void selectItem() {
        okuki.gotoPlace(new SwapiImagePlace(name.get()));
    }

}
