package com.bookstore.app.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bookstore.app.base.BookStoreActionBarBase;

public class AddTeacherActivity extends BookStoreActionBarBase{
	
	ImageView ivCaptureImage;
	EditText etTeacherFullName,etTeacherMobileNumber,etTeacherUserName,etInstitutionName;
	Button btnOk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_teacher);
	}

}
