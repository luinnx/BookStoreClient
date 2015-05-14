package com.bookstore.app.activity;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bookstore.app.base.AgentActionbarBase;

public class AgentTA_DA_Activity extends AgentActionbarBase implements
		OnClickListener {

	LinearLayout llParentContainer;
	ImageView ivAddCost;
	View child;
	ArrayList<LinearLayout> mViews = new ArrayList<LinearLayout>();
	int viewChildCount = 0;
	Button btnOk;
	EditText etPublishDate;
	
	private int year;
	private int month;
	private int day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_ta_da);
		initViews();
	}

	private void initViews() {
		llParentContainer = (LinearLayout) findViewById(R.id.rlTransportationCostContainer);
		ivAddCost = (ImageView) findViewById(R.id.ivAddCost);
		btnOk = (Button) findViewById(R.id.btnOk);

		
		ivAddCost.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.ivAddCost) {
			child = LayoutInflater.from(this).inflate(R.layout.ta_da_items,
					null);
			
			ImageView ivPublishDate = (ImageView) (child
					.findViewById(R.id.ivPublishDate));
			etPublishDate = (EditText) (child
					.findViewById(R.id.etPublishDate));
			ivPublishDate.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					final Calendar c = Calendar.getInstance();
					year = c.get(Calendar.YEAR);
					month = c.get(Calendar.MONTH);
					day = c.get(Calendar.DAY_OF_MONTH);
					showDialog(1);
					
				}
			});
			llParentContainer.addView(child);
			mViews.add((LinearLayout) child);
		} else if (view.getId() == R.id.btnOk) {

			for (LinearLayout layout : mViews) {
				for (int c = 0; c < layout.getChildCount(); c++) {
					View childView = layout.getChildAt(c);
					EditText etTourCost = (EditText) (childView
							.findViewById(R.id.etTourCost));

					EditText etPublishDate = (EditText) (childView
							.findViewById(R.id.etPublishDate));
					EditText etFrom = (EditText) (childView
							.findViewById(R.id.etFrom));
					EditText etTo = (EditText) (childView
							.findViewById(R.id.etTo));
					EditText etDistance = (EditText) (childView
							.findViewById(R.id.etDistance));
					EditText etCarName = (EditText) (childView
							.findViewById(R.id.etCarName));
					
					String date=etPublishDate.getText().toString();
					
					String cost = etTourCost.getText().toString();
					Log.d("m2m", cost+" "+date);
				}
			}

		}

	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			etPublishDate.setText(new StringBuilder().append(year)
					.append("-").append(month+1).append("-").append(day)
					.append(" "));

		}
	};

}
