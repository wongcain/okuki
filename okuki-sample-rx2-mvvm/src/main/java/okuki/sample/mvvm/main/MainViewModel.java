package okuki.sample.mvvm.main;

import android.databinding.ObservableField;

import okuki.sample.mvvm.R;
import okuki.sample.mvvm.common.ViewModel;

public class MainViewModel implements ViewModel {

    public final ObservableField<Integer> childLayout = new ObservableField<>();

    @Override
    public void onAttach() {
        childLayout.set(R.layout.view_hello);
    }

    @Override
    public void onDetach() {

    }
}
