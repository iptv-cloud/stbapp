package com.hidayah.iptv.player.model;

import com.google.gson.annotations.SerializedName;

public class ChannelUrlRes{

	@SerializedName("url")
	private String url;

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"ChannelUrlRes{" + 
			"url = '" + url + '\'' + 
			"}";
		}
}