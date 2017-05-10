package okuki.sample.mvvm.swapi.image;

import android.databinding.ObservableField;

import javax.inject.Inject;

import okuki.Okuki;
import okuki.rx2.RxOkuki;
import okuki.sample.mvvm.common.mvvm.BaseViewModel;
import okuki.sample.mvvm.common.rx.Errors;


public class SwapiImageViewModel extends BaseViewModel {

    @Inject
    Okuki okuki;

    @Inject
    SwapiImageDataManager swapiImageDataManager;

    public final ObservableField<String> imageUrl = new ObservableField<>();

    @Override
    public void onAttach() {
        super.onAttach();
        addToAutoDispose(
                RxOkuki.onPlace(okuki, SwapiImagePlace.class).subscribe(
                        place -> swapiImageDataManager.setQuery(place.getData()),
                        Errors.log()
                ),
                swapiImageDataManager.onLoadingStatus().subscribe(isLoading -> {
                    if(isLoading){
                        // TODO
                    } else {
                        if(!swapiImageDataManager.getItems().isEmpty()){
                            imageUrl.set(swapiImageDataManager.getItems().get(0).link());
                        }
                    }
                })
        );
    }
}
