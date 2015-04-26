package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class AgentJobList {
	@SerializedName("bookname")
	public String BookName;
	@SerializedName("teacherinstitute")
	public String TeacherInstituteName;
	@SerializedName("bookpic")
	public String BookPicUrl;
	@SerializedName("quantity")
	public int Quantity;
	@SerializedName("agentname")
	public String AgentFullName;
	@SerializedName("jobid")
	public int JobID;
	@SerializedName("agentaddress")
	public String AgentAddress;
}
