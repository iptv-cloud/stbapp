package com.hidayah.iptv.player.view;

import com.hidayah.iptv.player.model.ChannelsItem;

public interface ChannelItemClickListener {
    void onChannelLoad(ChannelsItem channelsItem, int position);
}
