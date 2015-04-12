package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class TeacherEntity {
	@SerializedName("_id")
	public int _id;
	@SerializedName("full_name")
	public String full_name;
	@SerializedName("user_name")
	public String user_name;
	@SerializedName("password")
	public String password;
	@SerializedName("institute")
	public String institute;
	@SerializedName("mobile_no")
	public String mobile_no;
}
