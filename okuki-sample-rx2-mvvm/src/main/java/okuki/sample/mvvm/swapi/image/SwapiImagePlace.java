package okuki.sample.mvvm.swapi.image;

import okuki.Place;
import okuki.PlaceConfig;
import okuki.sample.mvvm.R;
import okuki.sample.mvvm.common.mvvm.ComponentConfig;
import okuki.sample.mvvm.swapi.SwapiPlace;

@PlaceConfig(parent = SwapiPlace.class)
@ComponentConfig(layoutResId = R.layout.swapi_image, viewModelClass = SwapiImageViewModel.class)
public class SwapiImagePlace extends Place<String> {

    public SwapiImagePlace(String data) {
        super(data);
    }

}
