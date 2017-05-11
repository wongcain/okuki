package okuki.sample.mvvm.swapi.list;

import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;
import okuki.sample.mvvm.BR;
import okuki.sample.mvvm.R;

public class SwapiListItemBinding implements OnItemBind<SwapiListItemViewModel> {

    @Override
    public void onItemBind(ItemBinding itemBinding, int position, SwapiListItemViewModel item) {
        itemBinding.set(BR.vm, R.layout.swapi_list_item);
    }

}
