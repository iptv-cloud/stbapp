package com.hidayah.iptv.player.model;

import com.google.gson.annotations.SerializedName;


public class Meta{

	@SerializedName("total")
	private int total;

	@SerializedName("offset")
	private int offset;

	@SerializedName("limit")
	private int limit;

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
		return total;
	}

	public void setOffset(int offset){
		this.offset = offset;
	}

	public int getOffset(){
		return offset;
	}

	public void setLimit(int limit){
		this.limit = limit;
	}

	public int getLimit(){
		return limit;
	}

	@Override
 	public String toString(){
		return 
			"Meta{" + 
			"total = '" + total + '\'' + 
			",offset = '" + offset + '\'' + 
			",limit = '" + limit + '\'' + 
			"}";
		}
}