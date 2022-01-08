package com.hidayah.iptv.player.view;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hidayah.iptv.R;
import com.hidayah.iptv.player.model.epg.DataItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class EpgDataItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEpgTitle,tvEpgSchedule ;

        public EpgDataItemViewHolder(View view) {
            super(view);
            tvEpgTitle = view.findViewById(R.id.tvEpgTitle);
            tvEpgSchedule = view.findViewById(R.id.tvEpgSchedule);

        }

        public void setContentInfo(DataItem content) {
            tvEpgTitle.setText(content.getTitle());
            tvEpgSchedule.setText(getTime(content.getStartTime())+" - "+getTime(content.getEndTime())+"  .   "+ content.getVchipRating());
        }

        private String getTime(String dateTime){
            SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date startDate = format.parse(dateTime);
                SimpleDateFormat date_format = new SimpleDateFormat("h:mm a");
                return date_format.format(startDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
//            String[] strings = dateTime.split("T");
//            final String timePlusTimezone = strings[1];
//            String time1 = timePlusTimezone.replace("+","WW");
//            final String[] wws = time1.split("WW");
//            return wws[0];
        }
    }