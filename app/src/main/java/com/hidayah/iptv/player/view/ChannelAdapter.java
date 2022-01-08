package com.hidayah.iptv.player.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hidayah.iptv.R;
import com.hidayah.iptv.player.model.ChannelsItem;

import java.util.ArrayList;

public class ChannelAdapter  extends RecyclerView.Adapter<ChannelItemViewHolder> {

    public static int selectedPosition = -1;
    private ArrayList<ChannelsItem> channelsItems;
    private ChannelItemClickListener channelItemClickListener;


    public ChannelAdapter(ArrayList<ChannelsItem> channelsItems) {
        this.channelsItems = channelsItems;
    }

    public void setChannelItemClickListener(ChannelItemClickListener channelItemClickListener) {
        this.channelItemClickListener = channelItemClickListener;
    }

    @NonNull
    @Override
    public ChannelItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_item, parent, false);
        return new ChannelItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelItemViewHolder holder, int position) {
        boolean isSelcted = selectedPosition == position;
           holder.setContentInfo(channelsItems.get(position),isSelcted);
           holder.itemView.setClickable(false);
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (channelItemClickListener != null) {
                       channelItemClickListener.onChannelLoad(channelsItems.get(position),position);
                   }
               }
           });
           if(isSelcted){
               holder.itemView.requestFocus();
           }

    }



    @Override
    public int getItemCount() {
        return channelsItems.size();
    }

    public void addChannels(ArrayList<ChannelsItem> channels) {
        if (channels != null) {
            channelsItems.addAll(channels);
            notifyDataSetChanged();
        }

    }
}
