package com.hidayah.iptv.auth.model;

import com.google.gson.annotations.SerializedName;

public class SiteInfo{

	@SerializedName("name")
	private String name;

	@SerializedName("epg_server_url")
	private String epgServerUrl;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setEpgServerUrl(String epgServerUrl){
		this.epgServerUrl = epgServerUrl;
	}

	public String getEpgServerUrl(){
		return epgServerUrl;
	}

	@Override
 	public String toString(){
		return 
			"SiteInfo{" + 
			"name = '" + name + '\'' + 
			",epg_server_url = '" + epgServerUrl + '\'' + 
			"}";
		}
}