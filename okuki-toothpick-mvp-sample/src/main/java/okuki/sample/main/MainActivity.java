package okuki.sample.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.support.v7.widget.RxToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import okuki.sample.App;
import okuki.sample.R;
import okuki.sample.chucknorris.ChuckNorrisView;
import okuki.sample.kittens.KittensView;
import okuki.sample.welcome.WelcomeTextView;
import rx.Observable;

public class MainActivity extends AppCompatActivity implements MainPresenter.Vu {

    private final MainPresenter presenter = new MainPresenter();

    @BindView(R.id.main_container)
    ViewGroup mainContainer;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        presenter.attachVu(this);
    }

    @Override
    protected void onDestroy() {
        presenter.detachVu();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public Observable<Integer> onMenuSelection() {
        return RxToolbar.itemClicks(toolbar).map(menuItem -> menuItem.getItemId());
    }

    @Override
    public Observable<Void> onNavHomeClick() {
        return RxToolbar.navigationClicks(toolbar);
    }

    @Override
    public void loadWelcome() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mainContainer.removeAllViews();
        mainContainer.addView(new WelcomeTextView(this));
    }

    @Override
    public void loadCheckNorris() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainContainer.removeAllViews();
        mainContainer.addView(new ChuckNorrisView(this));
    }

    @Override
    public void loadKittens() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainContainer.removeAllViews();
        mainContainer.addView(new KittensView(this));
    }

    @Override
    public void onBackPressed() {
        if (!presenter.handleBack()) {
            super.onBackPressed();
        }
    }

}
