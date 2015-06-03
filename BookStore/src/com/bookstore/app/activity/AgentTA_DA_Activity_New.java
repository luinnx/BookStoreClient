package com.bookstore.app.activity;

import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

public class AgentTA_DA_Activity_New extends AgentActionbarBase implements
		OnClickListener, IAsynchronousTask {

	Button btnOk;
	EditText etPublishDate, etFrom, etTo, etDistance, etCarName, etTourPurpose,
			etTourCost, etOthersCost;
	TextView tvTotalCost;
	ImageView ivPublishDate;

	ImageView ivStartTime, ivEndTime;

	EditText etStartTime, etEndTime;
	private int whichMode = 1;

	private int year;
	private int month;
	private int day;

	private int hour;
	private int minute;

	DownloadableAsyncTask downloadAsyncTask;
	ProgressDialog dialog;
	
	
	DatePickerDialog datePickerDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_ta_da_new);
		initViews();
	}

	private void initViews() {

		tvTotalCost = (TextView) findViewById(R.id.tvTotalCost);

		etPublishDate = (EditText) findViewById(R.id.etPublishDate);
		etFrom = (EditText) findViewById(R.id.etFrom);

		etTo = (EditText) findViewById(R.id.etTo);
		etDistance = (EditText) findViewById(R.id.etDistance);
		etCarName = (EditText) findViewById(R.id.etCarName);
		etTourPurpose = (EditText) findViewById(R.id.etTourPurpose);
		etTourCost = (EditText) findViewById(R.id.etTourCost);
		etOthersCost = (EditText) findViewById(R.id.etOthersCost);

		etStartTime = (EditText) findViewById(R.id.etStartTime);
		etEndTime = (EditText) findViewById(R.id.etEndTime);

		ivPublishDate = (ImageView) findViewById(R.id.ivPublishDate);

		ivStartTime = (ImageView) findViewById(R.id.ivStartTime);
		ivEndTime = (ImageView) findViewById(R.id.ivEndTime);

		etTourCost.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (etOthersCost.getText().toString().trim().length() > 0) {
					int tourCost = Integer.parseInt(etTourCost.getText()
							.toString().trim());
					int otherCost = Integer.parseInt(etOthersCost.getText()
							.toString().trim());
					tvTotalCost.setText("" + (tourCost + otherCost));
				} else {
					int tourCost = Integer.parseInt(etTourCost.getText()
							.toString().trim());
					tvTotalCost.setText("" + tourCost);
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

		});

		etOthersCost.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (etTourCost.getText().toString().trim().length() > 0) {
					int tourCost = Integer.parseInt(etTourCost.getText()
							.toString().trim());
					int otherCost = Integer.parseInt(etOthersCost.getText()
							.toString().trim());
					tvTotalCost.setText("" + (tourCost + otherCost));
				} else {
					int tourCost = Integer.parseInt(etOthersCost.getText()
							.toString().trim());
					tvTotalCost.setText("" + tourCost);
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

		});

		btnOk = (Button) findViewById(R.id.btnOk);
		ivPublishDate.setOnClickListener(this);
		ivStartTime.setOnClickListener(this);
		ivEndTime.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	}

	@SuppressLint("InflateParams")
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.ivPublishDate) {
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			showDialog(1);
		} else if (view.getId() == R.id.btnOk) {

			/*
			 * http://localhost:8084/BookStoreService/api/agent/addTada?agentID=1
			 * &date=2015-02-22&startPlace=abc&startTime=10.00am
			 * &endPlace=xyz&endTime
			 * =12.00pm&description=sadjkhf&vehicelName=cng&
			 * distance=2.4&amount=220&otherAmount=20& totalAmount=240&status=p
			 */
			if (etFrom.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Enter Start Location");
				return;
			} else if (etTo.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Enter Destination Location");
				return;
			} else if (etTourPurpose.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Enter Tour Purpose");
				return;
			} else if (etDistance.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Enter Approximate Distance");
				return;
			} else if (etPublishDate.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Enter Tour Date");
				return;
			} else if (etTourCost.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Enter Tour Cost");
				return;
			}
			if (!CommonTasks.isOnline(this)) {
				CommonTasks.goSettingPage(this);
				return;
			}
			loadInformation();

		} else if (view.getId() == R.id.ivStartTime) {
			final Calendar c = Calendar.getInstance();
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
			whichMode = 2;
			showDialog(2);

		} else if (view.getId() == R.id.ivEndTime) {
			final Calendar c = Calendar.getInstance();
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);

			whichMode = 3;
			showDialog(3);
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			// set date picker as current date
			datePickerDialog=new DatePickerDialog(this, datePickerListener, year, month,
					day);
			datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
			return datePickerDialog;
			//return 
		case 2:
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);
		case 3:
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);
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

			etPublishDate.setText(new StringBuilder().append(year).append("-")
					.append(month + 1).append("-").append(day).append(" "));

		}
	};

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;

			String AM_PM;
			if (hour < 12) {
				AM_PM = "AM";
			} else {
				AM_PM = "PM";
			}

			// etStartTime,etEndTime
			if (whichMode == 2) {
				etStartTime.setText("" + hour);
				etStartTime.append("." + minute);
				etStartTime.append("" + AM_PM);
			} else if (whichMode == 3) {
				etEndTime.setText("" + hour);
				etEndTime.append("." + minute);
				etEndTime.append("" + AM_PM);
			}

		}
	};

	private void loadInformation() {
		if (downloadAsyncTask != null)
			downloadAsyncTask.cancel(true);
		downloadAsyncTask = new DownloadableAsyncTask(this);
		downloadAsyncTask.execute();

	}

	@Override
	public void showProgressBar() {
		dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		dialog.setMessage("Sending Request , Plaese wait...");
		dialog.setCancelable(false);
		dialog.show();

	}

	@Override
	public void hideProgressBar() {
		dialog.dismiss();

	}

	@Override
	public Object doInBackground() {
		IAgent agent = new AgentManager();
		return agent.submitTaDa(CommonTasks.getPreferences(
				getApplicationContext(), CommonConstraints.USER_ID),
				etPublishDate.getText().toString().trim(), etFrom.getText()
						.toString().trim(), etStartTime.getText().toString()
						.trim(), etTo.getText().toString().trim(), etEndTime
						.getText().toString().trim(), etTourPurpose.getText()
						.toString().trim(), etCarName.getText().toString()
						.trim(), etDistance.getText().toString().trim(),
				etTourCost.getText().toString().trim(), etOthersCost.getText()
						.toString().trim(), tvTotalCost.getText().toString()
						.trim(), "P");
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			boolean result = (Boolean) data;
			if (result) {
				CommonTasks.showToast(getApplicationContext(),
						"TA/DA Submission Succesfull.");
				super.onBackPressed();
			} else {
				CommonTasks.showToast(getApplicationContext(),
						"TA/DA Submission Failed. Please Try Again Later");
			}
		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please Try Again Later");
		}

	}

}
