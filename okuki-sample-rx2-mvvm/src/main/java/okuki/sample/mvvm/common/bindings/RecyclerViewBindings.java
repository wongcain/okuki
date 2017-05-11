package okuki.sample.mvvm.common.bindings;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapters;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

public class RecyclerViewBindings {

    @BindingAdapter(value = {"itemBinding", "items", "adapter", "itemIds", "viewHolder", "onItemBound"}, requireAll = false)
    public static <T> void setAdapter(RecyclerView recyclerView, OnItemBind<T> onItemBind, List<T> items,
                                      BindingRecyclerViewAdapter<T> adapter, BindingRecyclerViewAdapter.ItemIds<? super T> itemIds,
                                      BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory, OnItemBoundHandler<T> onItemBoundHandler) {
        ItemBinding<T> itemBinding = ((onItemBind!=null) && (onItemBoundHandler!=null))
                ? ItemBinding.of(new OnItemBindWrapper<>(onItemBind, onItemBoundHandler))
                : ItemBinding.of(onItemBind);
        BindingRecyclerViewAdapters.setAdapter(recyclerView, itemBinding, items, adapter, itemIds, viewHolderFactory);

    }

    public interface OnItemBoundHandler<T> {
        void onItemBound(int position);
    }

    private static class OnItemBindWrapper<T> implements OnItemBind<T> {

        private final OnItemBind<T> onItemBind;
        private final OnItemBoundHandler<T> onItemBoundHandler;

        public OnItemBindWrapper(OnItemBind<T> onItemBind, OnItemBoundHandler<T> onItemBoundHandler) {
            this.onItemBind = onItemBind;
            this.onItemBoundHandler = onItemBoundHandler;
        }

        @Override
        public void onItemBind(ItemBinding itemBinding, int position, T item) {
            onItemBind.onItemBind(itemBinding, position, item);
            onItemBoundHandler.onItemBound(position);
        }

    }

}