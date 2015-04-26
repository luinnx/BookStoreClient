package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class PushNotification {
	@SerializedName("Message")
	public String Message;
	@SerializedName("JobID")
	public int id;
}
