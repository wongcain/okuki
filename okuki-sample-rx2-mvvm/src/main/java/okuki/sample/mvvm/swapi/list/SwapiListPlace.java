package okuki.sample.mvvm.swapi.list;

import okuki.PlaceConfig;
import okuki.SimplePlace;
import okuki.sample.mvvm.R;
import okuki.sample.mvvm.common.mvvm.MvvmComponent;
import okuki.sample.mvvm.common.mvvm.ViewModel;
import okuki.sample.mvvm.swapi.SwapiPlace;

@PlaceConfig(parent = SwapiPlace.class)
public class SwapiListPlace extends SimplePlace implements MvvmComponent {

    @Override
    public int getLayoutResId() {
        return R.layout.swapi_list;
    }

    @Override
    public ViewModel getViewModel() {
        return new SwapiListViewModel();
    }

}
