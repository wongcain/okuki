package okuki.sample.mvvm.swapi.image;

import okuki.Place;
import okuki.PlaceConfig;
import okuki.sample.mvvm.R;
import okuki.sample.mvvm.common.mvvm.MvvmComponent;
import okuki.sample.mvvm.common.mvvm.ViewModel;
import okuki.sample.mvvm.swapi.SwapiPlace;

@PlaceConfig(parent = SwapiPlace.class)
public class SwapiImagePlace extends Place<String> implements MvvmComponent {

    public SwapiImagePlace(String data) {
        super(data);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.swapi_image;
    }

    @Override
    public ViewModel getViewModel() {
        return new SwapiImageViewModel(getData());
    }
}
