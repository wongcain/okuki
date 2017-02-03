package okuki.sample.chucknorris;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import okuki.sample.R;
import okuki.sample.common.rx.Errors;

public class ChuckNorrisView extends RelativeLayout implements ChuckNorrisPresenter.Vu {

    private final ChuckNorrisPresenter presenter = new ChuckNorrisPresenter();

    @BindView(R.id.cn_quote)
    TextView quote;

    public ChuckNorrisView(Context context) {
        super(context);
        init(context);
    }

    public ChuckNorrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChuckNorrisView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_chucknorris, this, true);
        ButterKnife.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.attachVu(this);
        RxView.clicks(this).subscribe(
                aVoid -> presenter.reload(),
                Errors.log()
        );
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.detachVu();
        super.onDetachedFromWindow();
    }

    @Override
    public void setJokeHtml(String jokeHtml) {
        quote.setText(Html.fromHtml(jokeHtml));
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (isLoading) quote.setText(R.string.loading_awesome);
    }

}
