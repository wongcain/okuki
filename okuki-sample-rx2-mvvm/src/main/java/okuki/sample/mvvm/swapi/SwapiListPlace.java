package okuki.sample.mvvm.swapi;

import okuki.Place;
import okuki.sample.mvvm.R;
import okuki.sample.mvvm.common.api.swapi.SwapiItem;
import okuki.sample.mvvm.common.api.swapi.SwapiItem.Type;
import okuki.sample.mvvm.common.mvvm.ComponentConfig;

@ComponentConfig(layoutResId = R.layout.swapi_list, viewModelClass = SwapiListViewModel.class)
public class SwapiListPlace extends Place<SwapiItem.Type> {

    public SwapiListPlace( Type data ) {
        super( data );
    }

}
