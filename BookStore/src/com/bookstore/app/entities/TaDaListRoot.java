package com.bookstore.app.entities;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class TaDaListRoot {
	@SerializedName("TadaList")
	public ArrayList<TadaListEntity> tadaList = null;
	
	public TaDaListRoot() {
		tadaList = new ArrayList<TadaListEntity>();
	}
}
