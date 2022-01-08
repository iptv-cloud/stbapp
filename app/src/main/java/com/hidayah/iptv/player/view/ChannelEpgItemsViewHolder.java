package com.hidayah.iptv.player.view;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hidayah.iptv.R;

import java.util.ArrayList;

public class ChannelEpgItemsViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ChannelEpgItemsViewHold";
        private RecyclerView rvChannelEpgItems;
        RelativeLayout rlChannelEpgItemChannelInfoCell;
        ImageView ivEpgChannelLogo;
        TextView tvChannelName;
        private EpgItemClickListener epgItemClickListener;
        private  EpgDataItemAdapter epgDataItemAdapter;

        public ChannelEpgItemsViewHolder(View view) {
            super(view);

            rlChannelEpgItemChannelInfoCell = view.findViewById(R.id.rlChannelEpgItemChannelInfoCell);
            ivEpgChannelLogo = view.findViewById(R.id.ivEpgChannelLogo);
            rvChannelEpgItems = view.findViewById(R.id.rvChannelEpgItems);
            tvChannelName = view.findViewById(R.id.tvChannelName);
            final LinearLayoutManager layout = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
            rvChannelEpgItems.setHasFixedSize(true);
            rvChannelEpgItems.setLayoutManager(layout);
            epgDataItemAdapter = new EpgDataItemAdapter(new ArrayList<>());
        }

        public void setContentInfo(int currentChannelId, com.hidayah.iptv.player.model.epg.ChannelsItem channelsItem) {
            if (channelsItem != null && channelsItem.getData() != null) {
                if (tvChannelName != null) {
                    tvChannelName.setText(channelsItem.getName());
                }
                if (channelsItem.getLogoUrl() != null) {
                    Glide.with(itemView.getContext()).load(channelsItem.getLogoUrl()).into(ivEpgChannelLogo);
                }
                tvChannelName.setText("Channel ID : " + channelsItem.getChannelId());
                epgDataItemAdapter.setCurrentChannelId(currentChannelId);
                epgDataItemAdapter.setIntParentChannelId(channelsItem.getChannelId());
                epgDataItemAdapter.setEpgItemClickListener(epgItemClickListener);
                rvChannelEpgItems.swapAdapter(epgDataItemAdapter,false);
                epgDataItemAdapter.addChannels(channelsItem.getData());
                Log.d(TAG, "setContentInfo() called with: dataItemList = [" + channelsItem.getData() + "]");
            }
        }

    public void setListener(EpgItemClickListener epgItemClickListener) {
            this.epgItemClickListener = epgItemClickListener;
    }
}