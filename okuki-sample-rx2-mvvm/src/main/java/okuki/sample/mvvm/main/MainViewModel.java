package okuki.sample.mvvm.main;

import android.databinding.ObservableField;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.sample.mvvm.common.api.swapi.SwapiItem;
import okuki.sample.mvvm.common.mvvm.BaseViewModel;
import okuki.sample.mvvm.common.mvvm.Component;
import okuki.sample.mvvm.common.mvvm.RxObservableField;
import okuki.sample.mvvm.common.mvvm.RxOkukiComponent;
import okuki.sample.mvvm.common.rx.Errors;
import okuki.sample.mvvm.swapi.SwapiListPlace;

public class MainViewModel extends BaseViewModel {

    @Inject
    Okuki okuki;

    public final List<SwapiItem.Type> swapiItemTypes = Arrays.asList(SwapiItem.Type.values());
    public final ObservableField<Integer> selectedSwapiItemType = new ObservableField();
    public final ObservableField<Component> mainComponent = new ObservableField<>();

    @Override
    public void onAttach() {
        super.onAttach();
        addToAutoDispose(
                RxObservableField.toObservable(selectedSwapiItemType).subscribe(
                        index -> okuki.gotoPlace(new SwapiListPlace(swapiItemTypes.get(index))),
                        Errors.log()
                ),
                RxOkukiComponent.onComponentBranch(okuki, SwapiListPlace.class).subscribe(
                        mainComponent::set,
                        Errors.log()
                )
        );
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
