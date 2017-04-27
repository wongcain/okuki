package okuki.sample.mvvm.swapi.list;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okuki.sample.mvvm.common.api.swapi.Swapi;
import okuki.sample.mvvm.common.api.swapi.SwapiItem;
import okuki.sample.mvvm.common.api.swapi.SwapiItem.Type;
import okuki.sample.mvvm.common.network.DataManager;

public class SwapiListDataManager extends DataManager<SwapiListItemViewModel> {

    @Inject
    Swapi swapi;

    private SwapiItem.Type swapiItemType = Type.values()[0];

    @Override
    protected Observable<List<SwapiListItemViewModel>> doLoad(int limit, int offsetIgnored ) {
        return swapi.getItems(swapiItemType).map( swapiItemPage -> {
            List<SwapiListItemViewModel> list = new ArrayList<>();
            for(SwapiItem item: swapiItemPage.results()){
                list.add(new SwapiListItemViewModel(item));
            }
            return list;
        } );
    }

    public void setSwapiItemType( Type swapiItemType ) {
        this.swapiItemType = swapiItemType;
        load();
    }

}
