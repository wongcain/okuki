package okuki.sample.mvvm.common.mvvm.recyclerview;

import okuki.sample.mvvm.common.mvvm.ViewModel;

public interface RecyclerItemBinder<VM extends ViewModel> {

    int getLayoutRes(VM viewModel);

    void onItemBound(int position, VM viewModel);

}
