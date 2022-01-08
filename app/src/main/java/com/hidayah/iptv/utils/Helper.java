package com.hidayah.iptv.utils;

import com.hidayah.iptv.player.model.epg.DataItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Helper {
    public static boolean isCurrentShow(DataItem dataItem) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date currentDate = new Date();
            Date start = format.parse(dataItem.getStartTime());
            Date end = format.parse(dataItem.getEndTime());
            //long diff = end.getTime() - start.getTime();//as given
            return start.before(currentDate) && currentDate.before(end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
