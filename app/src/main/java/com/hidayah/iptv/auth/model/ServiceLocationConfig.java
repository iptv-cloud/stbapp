package com.hidayah.iptv.auth.model;

import com.google.gson.annotations.SerializedName;

public class ServiceLocationConfig {

	@SerializedName("guide_url")
	private String guideUrl;

	@SerializedName("content_server")
	public String contentServer;

	@SerializedName("stream_server")
	public String streamServer;

	@SerializedName("time_zone")
	private String timeZone;
}