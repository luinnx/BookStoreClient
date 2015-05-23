package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class PushNotification {
	@SerializedName("Message")
	public String Message;
	@SerializedName("ID")
	public int id;
	@SerializedName("Status")
	public String Status;
}
