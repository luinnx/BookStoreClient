package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class TadaListEntity {
	@SerializedName("totalamount")
	public Double totalamount;
	@SerializedName("distance")
	public Double distance;
	@SerializedName("agentpic")
	public String agentpic;
	@SerializedName("agentname")
	public String agentname;
	@SerializedName("createdate")
	public long createdate;
	@SerializedName("id")
	public int id;
	@SerializedName("status")
	public String status;

}
