package com.bookstore.app.entities;

import com.google.gson.annotations.SerializedName;

public class ResponseEntity {
	@SerializedName("status")
	public boolean status;
	@SerializedName("message")
	public String message;

}
