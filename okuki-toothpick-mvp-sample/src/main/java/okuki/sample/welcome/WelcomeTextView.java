package okuki.sample.welcome;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import okuki.sample.R;

public class WelcomeTextView extends FrameLayout implements WelcomePresenter.Vu {

    private final WelcomePresenter presenter = new WelcomePresenter();

    @BindView(R.id.welcome_text)
    TextView welcomeText;

    public WelcomeTextView(Context context) {
        super(context);
        init(context);
    }

    public WelcomeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WelcomeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_welcome, this, true);
        ButterKnife.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.attachVu(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.detachVu();
        super.onDetachedFromWindow();
    }

    @Override
    public void setWelcomeMessage(String msg) {
        welcomeText.setText(msg);
    }
}
