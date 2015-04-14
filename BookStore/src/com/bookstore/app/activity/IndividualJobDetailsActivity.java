package com.bookstore.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bookstore.app.base.BookStoreActionBarBase;

public class IndividualJobDetailsActivity extends BookStoreActionBarBase implements OnClickListener {
	
	Button btnOk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_job_details);
		initViews();
		
	}
	private void initViews() {
		btnOk=(Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View view) {
		if(view.getId()==R.id.btnOk){
			super.onBackPressed();
		}
		
	}

	

}
