package okuki.sample.kittens;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;

import okuki.sample.common.rx.Errors;
import okuki.sample.kittens.detail.KittensDetailActivity;

public class KittensView extends RecyclerView implements KittensPresenter.Vu {

    private final KittensPresenter presenter = new KittensPresenter();

    public KittensView(Context context) {
        super(context);
        init(context);
    }

    public KittensView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KittensView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setLayoutManager(new GridLayoutManager(context, 2));
        setAdapter(new KittensAdapter());
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
    public void notifyUpdated() {
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void notifyInserted(int position, int count) {
        getAdapter().notifyItemRangeInserted(position, count);
    }

    @Override
    public void loadKittensDetails() {
        getContext().startActivity(new Intent(getContext(), KittensDetailActivity.class));
    }

    class KittenViewHolder extends ViewHolder {

        ImageView kittenImg;

        public KittenViewHolder(View itemView) {
            super(itemView);
            kittenImg = (ImageView) itemView;
        }

    }

    class KittensAdapter extends RecyclerView.Adapter<KittenViewHolder> {

        @Override
        public KittenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new KittenViewHolder(new ImageView(parent.getContext()));
        }

        @Override
        public void onBindViewHolder(KittenViewHolder holder, int position) {
            int size = getResources().getDisplayMetrics().widthPixels / 2;
            holder.kittenImg.setLayoutParams(new ViewGroup.LayoutParams(size, size));
            Glide.with(getContext()).load(presenter.getItemImage(position)).asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(holder.kittenImg);
            RxView.clicks(holder.kittenImg).subscribe(
                    click -> presenter.handleItemSelected(position),
                    Errors.log()
            );
        }

        @Override
        public int getItemCount() {
            return presenter.getNumItems();
        }
    }
}
