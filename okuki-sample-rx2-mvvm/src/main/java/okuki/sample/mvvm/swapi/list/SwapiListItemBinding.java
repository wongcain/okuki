package okuki.sample.mvvm.swapi.list;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;
import okuki.sample.mvvm.BR;
import okuki.sample.mvvm.R;

public class SwapiListItemBinding implements OnItemBind<SwapiListItemViewModel> {

    private static SwapiListItemBinding INSTANCE;

    private SwapiListItemBinding() {
    }

    @Override
    public void onItemBind(ItemBinding itemBinding, int position, SwapiListItemViewModel item) {
        itemBinding.set(BR.vm, R.layout.swapi_list_item);
    }

    public static SwapiListItemBinding get() {
        if(INSTANCE == null) INSTANCE = new SwapiListItemBinding();
        return INSTANCE;
    }

}
