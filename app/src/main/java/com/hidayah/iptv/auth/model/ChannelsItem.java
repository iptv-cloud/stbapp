package com.hidayah.iptv.auth.model;

import com.google.gson.annotations.SerializedName;

public class ChannelsItem implements Comparable<ChannelsItem>{

	@SerializedName("logo_path")
	private String logoPath;

	@SerializedName("channel_name")
	private String channelName;

	@SerializedName("guide_text")
	private String guideText;

	@SerializedName("channel_id")
	private int channelId;

	@SerializedName("stream_path")
	private String streamPath;

	@SerializedName("channel_number")
	private int channelNumber;

	@SerializedName("channel_type")
	private int channelType;

	@SerializedName("guide_description")
	private String guideDescription;

	public ServiceLocationConfig serviceLocationConfig;

	public void setLogoPath(String logoPath){
		this.logoPath = logoPath;
	}

	public String getLogoPath(){
		return logoPath;
	}

	public void setChannelName(String channelName){
		this.channelName = channelName;
	}

	public String getChannelName(){
		return channelName;
	}

	public void setGuideText(String guideText){
		this.guideText = guideText;
	}

	public String getGuideText(){
		return guideText;
	}

	public void setChannelId(int channelId){
		this.channelId = channelId;
	}

	public int getChannelId(){
		return channelId;
	}

	public void setStreamPath(String streamPath){
		this.streamPath = this.serviceLocationConfig.streamServer + this.streamPath;
	}

	public void updatePath(){
		this.streamPath = this.serviceLocationConfig.streamServer + this.streamPath;
		this.logoPath = this.serviceLocationConfig.contentServer + this.logoPath;
	}

	public String getStreamPath(){
		return this.streamPath;
	}

	public void setChannelNumber(int channelNumber){
		this.channelNumber = channelNumber;
	}

	public int getChannelNumber(){
		return channelNumber;
	}

	public void setChannelType(int channelType){
		this.channelType = channelType;
	}

	public int getChannelType(){
		return channelType;
	}

	public void setGuideDescription(String guideDescription){
		this.guideDescription = guideDescription;
	}

	public String getGuideDescription(){
		return guideDescription;
	}

	public void setUrlConfig(ServiceLocationConfig serviceLocationConfig){
		this.serviceLocationConfig = serviceLocationConfig;
	}

	public ServiceLocationConfig getUrlConfig(){
		return serviceLocationConfig;
	}



	@Override
	public String toString(){
		return
				"ChannelsItem{" +
						"logo_path = '" + logoPath + '\'' +
						",channel_name = '" + channelName + '\'' +
						",guide_text = '" + guideText + '\'' +
						",channel_id = '" + channelId + '\'' +
						",stream_path = '" + streamPath + '\'' +
						",channel_number = '" + channelNumber + '\'' +
						",channel_type = '" + channelType + '\'' +
						",guide_description = '" + guideDescription + '\'' +
						"}";
	}


	@Override
	public int compareTo(ChannelsItem channelsItem) {
		return this.channelNumber - channelsItem.getChannelNumber();
	}

}