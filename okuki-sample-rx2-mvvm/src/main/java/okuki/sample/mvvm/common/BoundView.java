package okuki.sample.mvvm.common;

import android.support.annotation.LayoutRes;

public interface BoundView {

    ViewModel createViewModel();

    @LayoutRes
    int getViewResId();

}
