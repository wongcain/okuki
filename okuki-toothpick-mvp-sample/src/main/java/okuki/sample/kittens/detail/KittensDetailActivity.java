package okuki.sample.kittens.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import okuki.sample.R;
import timber.log.Timber;

public class KittensDetailActivity extends AppCompatActivity implements KittensDetailPresenter.Vu {

    private final KittensDetailPresenter presenter = new KittensDetailPresenter();

    @BindView(R.id.kittens_detail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.kittens_detail_img)
    ImageView kittenImg;

    @BindView(R.id.kittens_detail_progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kittens_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter.attachVu(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        presenter.detachVu();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        presenter.handleBack();
        super.onBackPressed();
    }

    @Override
    public void loadImage(String url) {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(this).load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(kittenImg);
    }

}
