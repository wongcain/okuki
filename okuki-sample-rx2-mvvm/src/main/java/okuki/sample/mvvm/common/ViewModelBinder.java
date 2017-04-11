package okuki.sample.mvvm.common;

import android.databinding.BindingAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.ViewGroup;

import okuki.sample.mvvm.main.MainViewModel;
import okuki.sample.mvvm.R;
import okuki.sample.mvvm.hello.HelloViewModel;

public class ViewModelBinder {

    private static final String TAG = ViewModelBinder.class.getSimpleName();

    private static final SparseArray<Class> VM_MAP = new SparseArray<>();
    static {
        VM_MAP.put(R.layout.activity_main, MainViewModel.class);
        VM_MAP.put(R.layout.view_hello, HelloViewModel.class);
    }

    @BindingAdapter(value={"fm", "childLayout"})
    public static void loadChildFragment(ViewGroup viewGroup, FragmentManager fm, Integer childLayout){
        if(childLayout != null) {
            if (viewGroup.getTag() == null || !viewGroup.getTag().equals(childLayout)) {
                BoundViewFragment fragment = BoundViewFragment.newInstance(childLayout, VM_MAP.get(childLayout));
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(viewGroup.getId(), fragment);
                ft.commit();
                viewGroup.setTag(childLayout);
            }
        }
    }

}
