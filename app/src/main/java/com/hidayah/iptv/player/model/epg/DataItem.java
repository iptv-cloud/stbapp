package com.hidayah.iptv.player.model.epg;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	private String metadata;
	private int parenChannelId;

	@SerializedName("start_time")
	private String startTime;

	@SerializedName("sub_title")
	private String subTitle;

	@SerializedName("new_episode")
	private boolean newEpisode;

	@SerializedName("program_id")
	private int programId;

	@SerializedName("end_time")
	private String endTime;

	@SerializedName("description")
	private String description;

	@SerializedName("title")
	private String title;

	@SerializedName("channel_id")
	private int channelId;

	@SerializedName("vchip_rating")
	private String vchipRating;

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public String getStartTime(){
		return startTime;
	}

	public void setSubTitle(String subTitle){
		this.subTitle = subTitle;
	}

	public String getSubTitle(){
		return subTitle;
	}

	public void setNewEpisode(boolean newEpisode){
		this.newEpisode = newEpisode;
	}

	public boolean isNewEpisode(){
		return newEpisode;
	}

	public void setProgramId(int programId){
		this.programId = programId;
	}

	public int getProgramId(){
		return programId;
	}

	public void setEndTime(String endTime){
		this.endTime = endTime;
	}

	public String getEndTime(){
		return endTime;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setChannelId(int channelId){
		this.channelId = channelId;
	}

	public int getChannelId(){
		return channelId;
	}

	public void setVchipRating(String vchipRating){
		this.vchipRating = vchipRating;
	}

	public String getVchipRating(){
		return vchipRating;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public int getParenChannelId() {
		return parenChannelId;
	}

	public void setParenChannelId(int parenChannelId) {
		this.parenChannelId = parenChannelId;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"start_time = '" + startTime + '\'' + 
			",sub_title = '" + subTitle + '\'' + 
			",new_episode = '" + newEpisode + '\'' + 
			",program_id = '" + programId + '\'' + 
			",end_time = '" + endTime + '\'' + 
			",description = '" + description + '\'' + 
			",title = '" + title + '\'' + 
			",channel_id = '" + channelId + '\'' + 
			",vchip_rating = '" + vchipRating + '\'' + 
			"}";
		}
}