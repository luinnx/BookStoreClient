package com.bookstore.app.entities;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class TaDaListRoot {
	@SerializedName("TadaList")
	public ArrayList<TaDaEntity> tadaList = null;
	
	public TaDaListRoot() {
		tadaList = new ArrayList<TaDaEntity>();
	}
}
