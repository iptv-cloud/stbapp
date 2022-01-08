package com.hidayah.iptv.player.model.epg;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class EpgCurrentGuideRes {

	@SerializedName("result")
	private String result;

	@SerializedName("data")
	private ArrayList<DataItem> data;

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}

	public ArrayList<DataItem> getData() {
		return data;
	}

	public void setData(ArrayList<DataItem> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "EpgCurrentGuideRes{" +
				"result='" + result + '\'' +
				", data=" + data +
				'}';
	}
}