package com.hidayah.iptv.auth.model;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.annotations.SerializedName;
import com.hidayah.iptv.player.model.epg.DataItem;


public class Data{

	@SerializedName("channel_token")
	private String channelToken;

	@SerializedName("channels")
	private List<ChannelsItem> channels;

	// @SerializedName("site_info")
	// private SiteInfo siteInfo;

	@SerializedName("token")
	private String token;

	@SerializedName("service_location_config")
	private ServiceLocationConfig serviceLocationConfig;

	public void setChannelToken(String channelToken){
		this.channelToken = channelToken;
	}

	public String getChannelToken(){
		return channelToken;
	}

	public void setChannels(List<ChannelsItem> channels){
		this.channels = channels;
		for (ChannelsItem channel : channels){
			channel.serviceLocationConfig = serviceLocationConfig;
		}
	}

	// Generic method to get a sublist of a list between two specified indexes
	public static<T> List<T> sublist(List<T> list, int start, int end) {
		return list.subList(start, end + 1);
	}

	public List<ChannelsItem> getChannels(){
		for (ChannelsItem channel : channels){
			channel.serviceLocationConfig = serviceLocationConfig;
//			System.out.println(channel.serviceLocationConfig);
			channel.updatePath();
			ChannelsItem a = channel;
//			System.out.println(a);
		}
		List<ChannelsItem> sortedChannelList =
				channels.stream()
						.sorted(Comparator.comparingLong(ChannelsItem::getChannelId)
								.thenComparingLong(ChannelsItem::getChannelId))
						.collect(Collectors.toList());
		List<ChannelsItem> channelSubList = sublist(sortedChannelList,0,19);
		return channelSubList;
//		return channels;
	}

	// public void setSiteInfo(SiteInfo siteInfo){
	// 	this.siteInfo = siteInfo;
	// }

	// public SiteInfo getSiteInfo(){
	// 	return siteInfo;
	// }

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	public void setServiceLocationConfig(ServiceLocationConfig serviceLocationConfig){
		this.serviceLocationConfig = serviceLocationConfig;
	}

	public ServiceLocationConfig getServiceLocationConfig(){
		return serviceLocationConfig;
	}

	@Override
	public String toString(){
		return
				"Data{" +
						"channel_token = '" + channelToken + '\'' +
						",channels = '" + channels + '\'' +
						",token = '" + token + '\'' +
						"}";
	}
}