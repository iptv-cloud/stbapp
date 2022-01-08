package com.hidayah.iptv.player.model.epg;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;


public class EpgGuideRes{

	@SerializedName("result")
	private String result;

	@SerializedName("data")
	private ArrayList<ChannelsItem> channels;

	public ArrayList<ChannelsItem> getChannels(){
		return channels;
	}

	@Override
	public String toString(){
		return
				"EpgGuideResponse{" +
						"result = '" + result + '\'' +
						",channels = '" + channels + '\'' +
						"}";
	}
}