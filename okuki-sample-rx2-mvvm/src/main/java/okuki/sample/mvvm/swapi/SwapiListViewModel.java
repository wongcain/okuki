package okuki.sample.mvvm.swapi;

import android.databinding.ObservableField;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.rx2.RxOkuki;
import okuki.sample.mvvm.common.mvvm.BaseViewModel;
import okuki.sample.mvvm.common.rx.Errors;


public class SwapiListViewModel extends BaseViewModel {

    @Inject
    Okuki okuki;

    public final ObservableField<String> swapiTypeName = new ObservableField<>();

    @Override
    public void onAttach() {
        super.onAttach();
        RxOkuki.onPlace(okuki, SwapiListPlace.class).subscribe(
                place -> swapiTypeName.set(place.getData().toString()),
                Errors.log()
        );
    }

}
