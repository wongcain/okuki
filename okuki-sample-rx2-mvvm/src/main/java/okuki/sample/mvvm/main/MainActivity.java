package okuki.sample.mvvm.main;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import okuki.sample.mvvm.BR;
import okuki.sample.mvvm.R;


public class MainActivity extends AppCompatActivity {

    private MainViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        vm = new MainViewModel();
        binding.setVariable(BR.vm, vm);
        vm.onAttach();
    }

}
