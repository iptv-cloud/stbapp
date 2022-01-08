package com.hidayah.iptv.player.view;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hidayah.iptv.R;
import com.hidayah.iptv.player.model.epg.ChannelsItem;

import java.util.ArrayList;

public class EpgGuideAdapter extends RecyclerView.Adapter<ChannelEpgItemsViewHolder> {
    private static final String TAG = "EpgGuideAdapter";

    private EpgItemClickListener epgItemClickListener;
    ArrayList<com.hidayah.iptv.player.model.epg.ChannelsItem> epgChannelList = new ArrayList<>();
    private int currentChannelId;

    public EpgGuideAdapter() {
       // this.epgChannelList = epgChannelList;
    }

    public void setChannelItemClickListener(EpgItemClickListener epgItemClickListener) {
        this.epgItemClickListener = epgItemClickListener;
    }

    @NonNull
    @Override
    public ChannelEpgItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_epg_items, parent, false);
        final ChannelEpgItemsViewHolder channelEpgItemsViewHolder = new ChannelEpgItemsViewHolder(view);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) epgItemClickListener.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need three fix imageview in width
        int devicewidth = displaymetrics.widthPixels / 7;
        channelEpgItemsViewHolder.rlChannelEpgItemChannelInfoCell.getLayoutParams().width = devicewidth;
        channelEpgItemsViewHolder.setListener(epgItemClickListener);
        return channelEpgItemsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelEpgItemsViewHolder holder, int position) {
        ChannelsItem channelsItem = epgChannelList.get(position);
        holder.setContentInfo(currentChannelId,channelsItem);
    }



    @Override
    public int getItemCount() {
        return epgChannelList.size();
    }

    public void addEpgChannelList(ArrayList<com.hidayah.iptv.player.model.epg.ChannelsItem> epgChannelList){
        if (epgChannelList != null) {
            this.epgChannelList.clear();
            this.epgChannelList.addAll(epgChannelList);
            notifyDataSetChanged();
        }

    }

    public void setCurrentShowUI(int channelId) {
        currentChannelId = channelId;
        Log.d(TAG, "setCurrentShowUI() called with: channelId = [" + channelId + "]");
    }
}
