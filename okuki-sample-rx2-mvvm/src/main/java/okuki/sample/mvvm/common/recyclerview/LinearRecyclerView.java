package okuki.sample.mvvm.common.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class LinearRecyclerView extends RecyclerView {

    public LinearRecyclerView(Context context) {
        super(context);
        init();
    }

    public LinearRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinearRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
