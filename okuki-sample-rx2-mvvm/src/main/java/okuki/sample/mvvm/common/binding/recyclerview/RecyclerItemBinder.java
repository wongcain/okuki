package okuki.sample.mvvm.common.binding.recyclerview;


public interface RecyclerItemBinder<T> {

    int getLayoutRes(T item);

    void onItemBound(int position, T item);

}
