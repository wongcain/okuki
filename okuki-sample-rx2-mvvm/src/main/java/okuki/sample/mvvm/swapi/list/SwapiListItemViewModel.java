package okuki.sample.mvvm.swapi.list;

import android.databinding.ObservableField;

import okuki.sample.mvvm.common.api.swapi.SwapiItem;
import okuki.sample.mvvm.common.mvvm.BaseViewModel;

public class SwapiListItemViewModel extends BaseViewModel {

    public final ObservableField<String> name = new ObservableField<>();

    public SwapiListItemViewModel(SwapiItem swapiItem) {
        name.set(swapiItem.name());
    }
}
