package com.hidayah.iptv.player.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("channels")
	private List<ChannelsItem> channels;

	@SerializedName("meta")
	private Meta meta;

	public void setChannels(List<ChannelsItem> channels){
		this.channels = channels;
	}

	public List<ChannelsItem> getChannels(){
		return channels;
	}

	public void setMeta(Meta meta){
		this.meta = meta;
	}

	public Meta getMeta(){
		return meta;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"channels = '" + channels + '\'' + 
			",meta = '" + meta + '\'' + 
			"}";
		}
}