package okuki.sample.mvvm.common.mvvm;

public interface MvvmComponent {

    Object getTag();

    int getLayoutResId();

    ViewModel getViewModel();

}