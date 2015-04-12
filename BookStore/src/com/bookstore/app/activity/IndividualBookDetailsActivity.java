package com.bookstore.app.activity;

import android.os.Bundle;

import com.bookstore.app.base.BookStoreActionBarBase;

public class IndividualBookDetailsActivity extends BookStoreActionBarBase{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_details);
	}
}
