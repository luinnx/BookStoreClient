package com.bookstore.app.entities;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class ActivityEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("_id")
	public int id;
	@SerializedName("user_id")
    public int user_id;
	@SerializedName("activity_date")
    public String activity_date;
	@SerializedName("teacher_name")
    public String teacherName;
	@SerializedName("institute_name")
    public String institute_name;
	@SerializedName("inititute_address")
    public String inititute_address;
	@SerializedName("book_name")
    public String book_name;
	@SerializedName("book_quantity")
    public int book_quantity;
	@SerializedName("group_name")
    public String group_name;
	@SerializedName("teacher_mobile_number")
    public String teacher_mobile_number;
	@SerializedName("teacher_signature")
    public String teacher_signature;
	@SerializedName("district")
    public String district;
	@SerializedName("full_name")
	public String full_name;

}
