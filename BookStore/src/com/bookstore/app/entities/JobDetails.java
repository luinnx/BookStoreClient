package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class JobDetails {
	@SerializedName("publishername")
	public String BookPublisherName;
	@SerializedName("bookpic")
	public String BookPicUrl;
	@SerializedName("publishdate")
	public String BookPublishDate;
	@SerializedName("bookprice")
	public double BookPrice;
	@SerializedName("agentname")
	public String AgentFullName;
	@SerializedName("teachermobilenumber")
	public String TeacherMobileNumber;
	@SerializedName("institute")
	public String TeacherInstituteName;
	@SerializedName("authername")
	public String BookAutherName;
	@SerializedName("jobstatus")
	public boolean JobStatus;
	@SerializedName("bookname")
	public String BookName;
	@SerializedName("adminname")
	public String AdminFullName;
	@SerializedName("isbn")
	public String BookISBNNumber;
	@SerializedName("agentmobilenumber")
	public String AgentMobileNumber;
	@SerializedName("quantity")
	public int BookQuantity;
	@SerializedName("agentcurrentlocation")
	public String AgentCurrentLocation;
	@SerializedName("teachername")
	public String TeacherName;
	@SerializedName("teacherusername")
	public String TeacherUserName;
	@SerializedName("agentaddress")
	public String AgentAddress;
	
}
