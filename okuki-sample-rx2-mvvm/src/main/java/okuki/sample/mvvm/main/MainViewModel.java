package okuki.sample.mvvm.main;

import android.databinding.ObservableField;
import android.view.View;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.sample.mvvm.common.api.swapi.SwapiItem;
import okuki.sample.mvvm.common.mvvm.BaseViewModel;
import okuki.sample.mvvm.common.mvvm.Component;
import okuki.sample.mvvm.common.mvvm.RxOkukiComponent;
import okuki.sample.mvvm.common.rx.Errors;
import okuki.sample.mvvm.swapi.image.SwapiImagePlace;
import okuki.sample.mvvm.swapi.list.SwapiListPlace;

public class MainViewModel extends BaseViewModel {

    @Inject
    Okuki okuki;

    public final ObservableField<Component> mainComponent = new ObservableField<>();

    @Override
    public void onAttach() {
        super.onAttach();
        addToAutoDispose(
                RxOkukiComponent.onComponentBranch(okuki, SwapiListPlace.class).subscribe(
                        mainComponent::set,
                        Errors.log()
                ),
                RxOkukiComponent.onComponentBranch(okuki, SwapiImagePlace.class).subscribe(
                        mainComponent::set,
                        Errors.log()
                )
        );
        if(okuki.getCurrentPlace() == null){
            okuki.gotoPlace(new SwapiListPlace(SwapiItem.Type.people));
        }
    }

}
