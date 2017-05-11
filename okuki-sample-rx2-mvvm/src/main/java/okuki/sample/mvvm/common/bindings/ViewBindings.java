package okuki.sample.mvvm.common.bindings;

import android.databinding.BindingAdapter;
import android.view.View;

public class ViewBindings {

    private ViewBindings() {
        throw new AssertionError("No instances.");
    }

    @BindingAdapter("onClick")
    public static void onClick(View view, ClickHandler clickHandler) {
        view.setOnClickListener(view1 -> clickHandler.onClick());
    }

    public interface ClickHandler {
        void onClick();
    }

    @BindingAdapter("isGone")
    public static void isGone(View view, Boolean isGone) {
        view.setVisibility(isGone ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter("isInvisible")
    public static void isInvisible(View view, Boolean isInvisible) {
        view.setVisibility(isInvisible ? View.INVISIBLE : View.VISIBLE);
    }

}
