package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class JobEntity {
	@SerializedName("jobid")
	public int jobid;
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
	

	/*
	 * "publishername":"MIT", "bookpic":"/Images/Book/4.pnh",
	 * "publishdate":"2015-04-02", "bookprice":59.7, "agentname":"Sumit Saha",
	 * "teachermobilenumber":"01717100000", "institute":"Hard-Own",
	 * "authername":"Detal and Ditel", "jobstatus":false,
	 * "bookname":"How to Program", "adminname":"Mahbub Hasan", "isbn":"123",
	 * "agentmobilenumber":"01737186096", "agentcurrentlocation":null,
	 * "teachername":"Mr X", "agentaddress":"Saver"
	 */

}