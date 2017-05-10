package okuki.sample.mvvm.common.binding.fresco;

import android.databinding.BindingAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

public class FrescoBindings {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(SimpleDraweeView view, String imageUrl) {
        view.setImageURI(imageUrl);
    }

}
