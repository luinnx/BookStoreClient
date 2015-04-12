package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class BookEntity {
	@SerializedName("_id")
	public int _id;
	@SerializedName("full_name")
	public String full_name;
	@SerializedName("Auther_name")
	public String Auther_name;
	@SerializedName("publisher_name")
	public String publisher_name;
	@SerializedName("isbn_no")
	public String isbn_no;
	@SerializedName("quantity")
	public int quantity;
	@SerializedName("condition")
	public String condition;
	@SerializedName("price")
	public double price;
	@SerializedName("publish_date")
	public String publish_date;
	@SerializedName("avaible")
	public int avaible;
	@SerializedName("pic_url")
	public String pic_url;

}
