package okuki.sample.mvvm.hello;

import android.databinding.ObservableField;

import okuki.sample.mvvm.R;
import okuki.sample.mvvm.common.ViewModel;

public class HelloViewModel implements ViewModel {
    public final ObservableField<String> message = new ObservableField<>();
    public final ObservableField<Integer> childLayout = new ObservableField<>();

    public void toggleView(){
        childLayout.set(childLayout.get() == R.layout.test1 ? R.layout.test2 : R.layout.test1);
    }

    @Override
    public void onAttach() {
        childLayout.set(R.layout.test1);
    }

    @Override
    public void onDetach() {

    }
}
