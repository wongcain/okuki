package okuki.sample.mvvm.common.mvvm;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import okuki.toothpick.ScopeConfig;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


@AutoValue
public abstract class Component {

    public static Component create(int layoutResId, @Nullable Class<? extends ViewModel> viewModelClass){
        return new AutoValue_Component(layoutResId, viewModelClass);
    }

    public static Component create(ComponentConfig config){
        return new AutoValue_Component(config.layoutResId(), config.viewModelClass());
    }

    public abstract int layoutResId();

    public @Nullable abstract Class<? extends ViewModel> viewModelClass();

    public String getTag(){
        return String.format("%s_%s", layoutResId(), viewModelClass());
    }

}