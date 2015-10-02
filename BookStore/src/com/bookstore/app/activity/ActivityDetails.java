package com.bookstore.app.activity;

import com.bookstore.app.entities.ActivityEntity;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityDetails extends Activity{
	ActivityEntity activityEntity;
	TextView tvTeacherName, tvInstitudeName, tvFaculty, tvDistrict, tvMobileNumber, tvBookName, tvQuantity, tvActivityDate;
	Button btnEdit, btnSubmit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		initViews();
	}
	
	private void initViews(){
		activityEntity=(ActivityEntity) getIntent().getExtras().get("ACTIVITY");
		
		tvTeacherName = (TextView) findViewById(R.id.tvTeacherName);
		tvInstitudeName = (TextView) findViewById(R.id.tvInstitudeName);
		tvFaculty = (TextView) findViewById(R.id.tvFaculty);
		tvDistrict = (TextView) findViewById(R.id.tvDistrict);
		tvMobileNumber = (TextView) findViewById(R.id.tvMobileNumber);
		tvBookName = (TextView) findViewById(R.id.tvBookName);
		tvQuantity = (TextView) findViewById(R.id.tvQuantity);
		tvActivityDate = (TextView) findViewById(R.id.tvActivityDate);
		
		tvTeacherName.setText(activityEntity.teacherName);
		tvInstitudeName.setText(activityEntity.institute_name);
		tvFaculty.setText(activityEntity.district);
		tvDistrict.setText(activityEntity.district);
		tvMobileNumber.setText(activityEntity.teacher_mobile_number);
		tvBookName.setText(activityEntity.book_name);
		tvQuantity.setText(""+activityEntity.book_quantity);
		tvActivityDate.setText(activityEntity.activity_date);
		
		

		btnEdit = (Button) findViewById(R.id.btnEdit);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		
		btnEdit.setText("OK");
		btnSubmit.setVisibility(View.GONE);
		
		
		btnEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				onBackPressed();
				
			}
		});
	}
}
