package com.hidayah.iptv.player.view;

import android.app.Activity;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hidayah.iptv.R;
import com.hidayah.iptv.player.model.epg.DataItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hidayah.iptv.utils.Helper.isCurrentShow;

public class EpgDataItemAdapter extends RecyclerView.Adapter<EpgDataItemViewHolder> {

    public static int selectedPosition = -1;
    private  int currentChannelId;
    private int intParentChannelId = -1;
    private List<DataItem> dataItems;
    private EpgItemClickListener epgItemClickListener;



    public EpgDataItemAdapter(List<DataItem> dataItemList) {
        this.dataItems = dataItemList;

    }

    public void setCurrentChannelId(int currentChannelId) {
        this.currentChannelId = currentChannelId;
    }

    public void setIntParentChannelId(int intParentChannelId) {
        this.intParentChannelId = intParentChannelId;
    }

    public EpgItemClickListener getEpgItemClickListener() {
        return epgItemClickListener;
    }

    public void setEpgItemClickListener(EpgItemClickListener epgItemClickListener) {
        this.epgItemClickListener = epgItemClickListener;
    }

    @NonNull
    @Override
    public EpgDataItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.epg_data_item, parent, false);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) epgItemClickListener.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //if you need three fix imageview in width
        int devicewidth = displaymetrics.widthPixels / 7;
        view.getLayoutParams().width = devicewidth*(viewType/30);
        return new EpgDataItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpgDataItemViewHolder holder, int position) {
        final DataItem dataItems = this.dataItems.get(position);
        final int timeDiffMin = getTimeDiffMin(dataItems);
//        if(timeDiffMin > 0){
//            holder.itemView.getLayoutParams().width = holder.itemView.getRootView().getWidth() / (180 / timeDiffMin);
//        }
           if(currentChannelId == dataItems.getChannelId() && isCurrentShow(dataItems)){
               holder.itemView.setBackgroundColor(Color.MAGENTA);
               holder.itemView.requestFocus();
           }
           holder.setContentInfo(dataItems);
           holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
               @Override
               public void onFocusChange(View view, boolean b) {
                   if(b){
                       epgItemClickListener.onEpgItemFocused(view,dataItems,position);
                   }
               }
           });
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   epgItemClickListener.onEpgItemClick(dataItems,intParentChannelId);
               }
           });
    }



    private static final String TAG = "EpgDataItemAdapter";

    private int getTimeDiffMin(DataItem dataItems) {
        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            Date start = format.parse(dataItems.getStartTime());
            Date end = format.parse(dataItems.getEndTime());
            long diff = end.getTime() - start.getTime();//as given
            int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(diff);
            return minutes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        final int timeDiffMin = getTimeDiffMin(dataItems.get(position));
        return timeDiffMin;
    }

    public void addChannels(List<DataItem> channels) {
        if (channels != null) {
            dataItems.clear();
            dataItems.addAll(channels);
            notifyDataSetChanged();
        }

    }
}
