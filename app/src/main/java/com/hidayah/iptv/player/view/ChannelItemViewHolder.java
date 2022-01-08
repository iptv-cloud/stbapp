package com.hidayah.iptv.player.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hidayah.iptv.R;
import com.hidayah.iptv.player.model.ChannelsItem;

public class ChannelItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private RelativeLayout selectedOverlay;
        private ChannelsItem channel;

        public ChannelItemViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.ivChannelLogo);
            textView = view.findViewById(R.id.tvChannelName);
            selectedOverlay = view.findViewById(R.id.selectedOverlay);

        }

        public void setContentInfo(ChannelsItem content,boolean isSelected) {
            this.channel = content;
            textView.setText(content.getName());
            updateCardViewImage(channel.getLogo());
            selectedOverlay.setVisibility(isSelected?View.VISIBLE:View.GONE);
        }



        protected void updateCardViewImage(String logo) {

            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.diskCacheStrategy(DiskCacheStrategy.ALL);
          //  options.placeholder(R.drawable.default_placeholder);

            Glide.with(imageView.getContext())
                    .load(logo)
                    .apply(options)
                    .into(imageView);
        }
    }