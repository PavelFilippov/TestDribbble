package ru.com.testdribbble.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import ru.com.testdribbble.R;
import ru.com.testdribbble.core.data.model.Shot;
import ru.com.testdribbble.ui.adapters.base.BaseAdapter;
import ru.com.testdribbble.ui.adapters.base.BaseViewHolder;
import ru.com.testdribbble.ui.main.shots.IOnViewInItemClick;
import ru.com.testdribbble.views.ProfileImageProgressBar;

public class ShotsAdapter extends BaseAdapter<Shot, ShotsAdapter.Holder> {

    private int itemHeight;
    private String authorName;
    private IOnViewInItemClick onViewInItemClick;

    public ShotsAdapter(
            Context context,
            int itemHeight,
            String authorName,
            IOnViewInItemClick onViewInItemClick) {
        super(context);
        this.itemHeight = itemHeight;
        this.authorName = authorName;
        this.onViewInItemClick = onViewInItemClick;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflateView(R.layout.item_shot, parent);
        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        lp.height = itemHeight;
        itemView.setLayoutParams(lp);
        return new Holder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return getData().get(position).getId();
    }

    @Override
    public void onBindViewHolderInternal(Holder holder, int position) {
        Shot shot = getItemById(position);
        String filePath = "";

        holder.txtAuthorName.setText(authorName != null ? authorName : "");
        holder.txtShotTitle.setText(shot.getTitle() != null ? shot.getTitle() : "");
        holder.txtShotDescription.setText(Html.fromHtml(shot.getDescription() != null ? shot.getDescription() : ""));

        if(shot.getImages().getHidpi() != null) {
            filePath = shot.getImages().getHidpi();
        } else if(shot.getImages().getTwoX() != null) {
            filePath = shot.getImages().getTwoX();
        } else if(shot.getImages().getNormal() != null) {
            filePath = shot.getImages().getNormal();
        } else if(shot.getImages().getOneX() != null) {
            filePath = shot.getImages().getOneX();
        } else if(shot.getImages().getTeaser() != null) {
            filePath = shot.getImages().getTeaser();
        }

        if (TextUtils.isEmpty(filePath)) {
            holder.sdvShot.setVisibility(View.GONE);
            holder.imgNoImage.setVisibility(View.VISIBLE);
        } else {
            holder.imgNoImage.setVisibility(View.GONE);
            holder.sdvShot.setVisibility(View.VISIBLE);
            holder.sdvShot.getHierarchy().setProgressBarImage(new ProfileImageProgressBar());
            holder.sdvShot.setImageURI(Uri.parse(filePath));
        }

        setupListener(holder.itemView, shot, position);

        holder.txtAuthorName.setOnClickListener(v -> onViewInItemClick.onAuthorNameClick(position));

    }

    class Holder extends BaseViewHolder {

        private SimpleDraweeView sdvShot;
        private AppCompatImageView imgNoImage;
        private TextView txtAuthorName;
        private TextView txtShotTitle;
        private TextView txtShotDescription;

        public Holder(View itemView) {
            super(itemView);
            sdvShot = getView(R.id.sdvShot);
            imgNoImage = getView(R.id.imgNoImage);
            txtAuthorName = getView(R.id.txtAuthorName);
            txtShotTitle = getView(R.id.txtShotTitle);
            txtShotDescription = getView(R.id.txtShotDescription);
        }
    }
}
