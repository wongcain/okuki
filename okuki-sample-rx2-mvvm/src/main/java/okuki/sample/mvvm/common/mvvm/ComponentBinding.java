package okuki.sample.mvvm.common.mvvm;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;

import okuki.sample.mvvm.BR;
import timber.log.Timber;

public class ComponentBinding {

    private ComponentBinding() {
        throw new AssertionError("No instances.");
    }

    @BindingAdapter("component")
    public static void loadChildComponent(ViewGroup viewGroup, Component component){
        if( component != null){
            String tag = component.getTag();
            if (!tag.equals(viewGroup.getTag())) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(component.layoutResId(), viewGroup, false);
                Class<? extends ViewModel> viewModelClass = component.viewModelClass();
                if(viewModelClass != null){
                    try {
                        ViewDataBinding binding = DataBindingUtil.bind( view );
                        if ( binding != null ) {
                            final ViewModel vm = viewModelClass.newInstance();
                            binding.setVariable( BR.vm, vm );
                            vm.onAttach(); //TODO why do I need to do this?
//                            view.addOnAttachStateChangeListener( new OnAttachStateChangeListener() {
//                                @Override public void onViewAttachedToWindow( View view ) {
//                                    vm.onAttach();
//                                }
//
//                                @Override public void onViewDetachedFromWindow( View view ) {
//                                    vm.onDetach();
//                                }
//                            } );
                        }
                    } catch (Exception e){
                        Timber.e(e, "Error getting binding");
                    }
                }
                viewGroup.removeAllViews();
                viewGroup.addView(view);
            }
        }
    }

}
