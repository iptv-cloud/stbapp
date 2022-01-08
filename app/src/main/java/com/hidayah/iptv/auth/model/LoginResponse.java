package com.hidayah.iptv.auth.model;

import android.os.Build;
import android.provider.Settings;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.hidayah.iptv.R;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class LoginResponse{

	@SerializedName("result")
	private String result;

	@SerializedName("data")
	private Data data;

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"LoginResponse{" + 
			"result = '" + result + '\'' + 
			",data = '" + data + '\'' + 
			"}";
		}
}