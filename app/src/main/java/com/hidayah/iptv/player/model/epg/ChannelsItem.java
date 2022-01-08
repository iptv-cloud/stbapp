package com.hidayah.iptv.player.model.epg;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ChannelsItem {

	@SerializedName("programs")
	public List<DataItem> data;

	@SerializedName("logo_url")
	private String logoUrl;

	@SerializedName("channel_id")
	private int channelId;

	@SerializedName("name")
	private String name;

	public void setChannelName(String channelName){
		this.name = channelName;
	}

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	public void setLogoUrl(String logoUrl){
		this.logoUrl = logoUrl;
	}

	public String getLogoUrl(){
		return logoUrl;
	}

	public void setChannelId(int channelId){
		this.channelId = channelId;
	}

	public int getChannelId(){
		return channelId;
	}

	public void appendData(DataItem dataItem){
		if (this.data == null) {
			List<DataItem> temp = new ArrayList<>(Arrays.asList(dataItem));
			this.setData(temp);
		}
		else {
			this.data.add(dataItem);
		}
	}

	@Override
 	public String toString(){
		return 
			"ChannelsItem{" + 
			"data = '" + data + '\'' + 
			",logo_url = '" + logoUrl + '\'' + 
			",channel_id = '" + channelId + '\'' + 
			"}";
		}

	public String getName() {
		return name;
	}
}