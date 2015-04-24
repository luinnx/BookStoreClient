package com.bookstore.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DonationListRoot {
	@SerializedName("DonationList")
	public List<DonationEntity> donationList;

	public DonationListRoot() {
		donationList = new ArrayList<DonationEntity>();
	}

}
