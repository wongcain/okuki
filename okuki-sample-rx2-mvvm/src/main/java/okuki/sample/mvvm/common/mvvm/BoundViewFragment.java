package okuki.sample.mvvm.common.mvvm;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import okuki.sample.mvvm.BR;

public class BoundViewFragment extends Fragment implements BoundView {

    private static final String LAYOUT_ID = "layout_id";
    private static final String VM_CLASS = "vm_class";
    private ViewModel vm;

    public static BoundViewFragment newInstance(@LayoutRes int layoutId, @NonNull Class<? extends ViewModel> vmClass){
        Log.d("ERROR", "newInstance");
        BoundViewFragment fragment = new BoundViewFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID, layoutId);
        args.putSerializable(VM_CLASS, vmClass);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        vm = createViewModel();
        if(vm != null) {
            vm.onAttach();
        }
    }

    @Override
    public void onDetach() {
        if(vm != null) {
            vm.onDetach();
        }
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(getViewResId(), container, false);
            ViewDataBinding binding = DataBindingUtil.bind(view);
            if(binding != null){
                binding.setVariable(BR.vm, vm);
                binding.setVariable(BR.fm, getChildFragmentManager());
            }
        } catch (Exception e) {
            Log.e("ERROR", "" + getViewResId());
        }
        return view;
    }

    @Override
    public ViewModel createViewModel() {
        ViewModel vm = null;
        Class<? extends ViewModel> vmClass = (Class<? extends ViewModel>) getArguments().getSerializable(VM_CLASS);
        try {
            vm = vmClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace(); //TODO
        }
        return vm;
    }

    @Override
    public int getViewResId() {
        return getArguments().getInt(LAYOUT_ID);
    }

}
