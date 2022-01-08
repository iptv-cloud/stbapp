package com.hidayah.iptv.player.view;

import android.content.Context;
import android.view.View;

import com.hidayah.iptv.player.model.epg.DataItem;

interface EpgItemClickListener {
    void onEpgItemClick(DataItem dataItem, int intParentChannelId);
    Context getContext();
    void onEpgItemFocused(View v, DataItem dataItems, int pos);
}
