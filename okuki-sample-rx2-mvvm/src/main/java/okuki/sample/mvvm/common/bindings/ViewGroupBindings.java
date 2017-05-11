package okuki.sample.mvvm.common.bindings;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import okuki.sample.mvvm.BR;
import okuki.sample.mvvm.common.mvvm.MvvmComponent;

public class ViewGroupBindings {

    private ViewGroupBindings() {
        throw new AssertionError("No instances.");
    }

    @BindingAdapter("component")
    public static void loadComponent(ViewGroup viewGroup, MvvmComponent component) {
        if (component != null) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), component.getLayoutResId(), viewGroup, false);
            View view = binding.getRoot();
            binding.setVariable(BR.vm, component.getViewModel());
            binding.executePendingBindings();
            viewGroup.removeAllViews();
            viewGroup.addView(view);
        }
    }

}
