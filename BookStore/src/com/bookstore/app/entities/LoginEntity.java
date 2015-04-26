package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class LoginEntity {

	@SerializedName("id")
	public int id;
	@SerializedName("status")
	public boolean status;
	@SerializedName("message")
	public String message;
	@SerializedName("type")
	public int type;
	
	//{"status":true,"message":"Login Successfully!","type":1,"id":1}

}
