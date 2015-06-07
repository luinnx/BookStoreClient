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
	@SerializedName("donationstatus")
	public int donationstatus;
}


/*"donationstatus":2,
"agentid":2,
"amount":500.0,
"agentfullname":"Sajedul Karim",
"agentpic":"",
"gcmid":"APA91bF_lSZbmN3ROtotVanYk76UJ4AA8O4kSf-8B6aXFes8ZjWXgqEp98qHw8_UUzawECB7EqkmIxksBBy_bQG8hXfmLDGlzvOUEfGFrynGIpeyOY9UlSea4hAXXivLnStax510uF1cyjRAAmx2OLfqA7okFNtaAw",
"donationdate":1433460855000,
"comment":"à¦¬à¦‡ à¦¬à¦¿à¦•à§à¦°à¦¿ à¦œà¦¨à§à¦¯",
"id":10*/
