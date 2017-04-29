package okuki.sample.mvvm.common.mvvm.recyclerview;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import java.util.Collection;

import okuki.sample.mvvm.common.mvvm.ViewModel;

public class RecyclerViewBindings
{
    private static final int KEY_ITEMS = -123;

    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static <VM extends ViewModel> void setItems(RecyclerView recyclerView, Collection<VM> items)
    {
        BindingRecyclerViewAdapter<VM> adapter = (BindingRecyclerViewAdapter<VM>) recyclerView.getAdapter();
        if (adapter != null)
        {
            adapter.setItems(items);
        }
        else
        {
            recyclerView.setTag(KEY_ITEMS, items);
        }
    }

    @SuppressWarnings("unchecked")
    @BindingAdapter("itemBinder")
    public static <VM extends ViewModel> void setItemBinder( RecyclerView recyclerView, RecyclerItemBinder<VM> itemViewMapper)
    {
        Collection<VM> items = (Collection<VM>) recyclerView.getTag(KEY_ITEMS);
        BindingRecyclerViewAdapter<VM> adapter = new BindingRecyclerViewAdapter<>(itemViewMapper, items);
        recyclerView.setAdapter(adapter);
    }
}