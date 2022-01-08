package com.hidayah.iptv.player.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
public class ChannelsItem implements Parcelable {

	@SerializedName("uid")
	private String uid;

	@SerializedName("name")
	private String name;

	@SerializedName("active")
	private boolean active;

	@SerializedName("logo")
	private String logo;

	@SerializedName("epg")
	private String epg;

	@SerializedName("slug")
	private String slug;

	public void setUid(String uid){
		this.uid = uid;
	}

	public String getUid(){
		return uid;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setActive(boolean active){
		this.active = active;
	}

	public boolean isActive(){
		return active;
	}

	public void setLogo(String logo){
		this.logo = logo;
	}

	public String getLogo(){
		return logo;
	}

	public void setEpg(String epg){
		this.epg = epg;
	}

	public String getEpg(){
		return epg;
	}

	public void setSlug(String slug){
		this.slug = slug;
	}

	public String getSlug(){
		return slug;
	}

	@Override
	public String toString(){
		return
				"ChannelsItem{" +
						"uid = '" + uid + '\'' +
						",name = '" + name + '\'' +
						",active = '" + active + '\'' +
						",logo = '" + logo + '\'' +
						",epg = '" + epg + '\'' +
						",slug = '" + slug + '\'' +
						"}";
	}

	protected ChannelsItem(Parcel in) {
		uid = in.readString();
		name = in.readString();
		active = in.readByte() != 0x00;
		logo = in.readString();
		epg = in.readString();
		slug = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(uid);
		dest.writeString(name);
		dest.writeByte((byte) (active ? 0x01 : 0x00));
		dest.writeString(logo);
		dest.writeString(epg);
		dest.writeString(slug);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<ChannelsItem> CREATOR = new Parcelable.Creator<ChannelsItem>() {
		@Override
		public ChannelsItem createFromParcel(Parcel in) {
			return new ChannelsItem(in);
		}

		@Override
		public ChannelsItem[] newArray(int size) {
			return new ChannelsItem[size];
		}
	};
}