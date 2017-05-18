package okuki.sample.mvvm.common.bindings;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;

public class SimpleDraweeViewBindings {

    private SimpleDraweeViewBindings() {
        throw new AssertionError("No instances.");
    }

    @BindingAdapter(value = {"imageUrl", "loResImageUrl"}, requireAll = false)
    public static void loadImage(final SimpleDraweeView view, String imageUrl, String loResImageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                    super.onIntermediateImageSet(id, imageInfo);
                    view.setVisibility(View.VISIBLE);
                }
            };

            PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setImageRequest(ImageRequest.fromUri(Uri.parse(imageUrl)))
                    .setOldController(view.getController());
            if (!TextUtils.isEmpty(loResImageUrl)) {
                controllerBuilder.setLowResImageRequest(ImageRequest.fromUri(Uri.parse(loResImageUrl)));
            }
            //view.setVisibility(View.GONE);
            view.setController(controllerBuilder.build());
        }
    }

}
