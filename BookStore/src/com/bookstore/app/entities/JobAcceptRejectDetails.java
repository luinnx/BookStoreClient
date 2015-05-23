package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class JobAcceptRejectDetails {
	
	@SerializedName("teachersignature")
	public String teachersignature;
	@SerializedName("agentgcmid")
	public String agentgcmid;
	@SerializedName("jobid")
	public int jobid;
	@SerializedName("bookid")
	public int bookid;
	@SerializedName("asignedbook")
	public int no_of_book;
	@SerializedName("bookname")
	public String bookname;
	@SerializedName("quantity")
	public int quantity;
	@SerializedName("teacherinstitute")
	public String teacherinstitute;
	@SerializedName("bookpic")
	public String bookImage;
	@SerializedName("agentname")
	public String agentname;
	@SerializedName("agentaddress")
	public String agentaddress;

	@SerializedName("publishername")
	public String publishername;
	@SerializedName("publishdate")
	public String publishdate;
	@SerializedName("bookprice")
	public float bookprice;
	@SerializedName("teachermobilenumber")
	public String teachermobilenumber;

	@SerializedName("institute")
	public String institute;
	@SerializedName("jobstatus")
	public boolean jobstatus;
	@SerializedName("isbn")
	public String isbn;
	@SerializedName("agentmobilenumber")
	public String agentmobilenumber;

	@SerializedName("teachername")
	public String teachername;
	@SerializedName("authername")
	public String authername;
	@SerializedName("agentcurrentlocation")
	public String agentcurrentlocation;
}
