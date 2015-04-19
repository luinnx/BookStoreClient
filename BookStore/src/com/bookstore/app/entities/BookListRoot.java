package com.bookstore.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class BookListRoot {
	@SerializedName("BookList")
	public List<BookEntity> bookList;
	
	public BookListRoot(){
		bookList=new ArrayList<BookEntity>();
	}

}
