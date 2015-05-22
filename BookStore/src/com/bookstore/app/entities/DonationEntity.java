package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class DonationEntity {
	
	@SerializedName("amount")
	public double Amount;
	@SerializedName("comment")
	public String comment;
	@SerializedName("agentfullname")
	public String agent_name;
	@SerializedName("donationdate")
	public long date;
	@SerializedName("agentpic")
	public String pic_url;
	@SerializedName("agentid")
	public int agentid;
	@SerializedName("id")
	public int id;
	@SerializedName("gcmid")
	public String gcmid;
}


/*"agentid":2,
"amount":220.0,
"agentfullname":"Sajedul Karim",
"agentpic":"",
"gcmid":"APA91bFSiiXdMkbxqHF5tT-PrAurtPuFvw4tWXtg3eOpJVt8ac2KyOgQqRZCagvZtEIxazt0KkTvxVRQdGu8bo5OkaOwdAwmb1wtF7MiwoBWzJYfxhuasLljclOlLhfGsI60HgKiid1U-liPOUzao5nYimHPg4w8Kw",
"donationdate":1432176157000,
"comment":"For selling more 3 books",
"id":1*/