package com.bookstore.app.activity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.entities.TaDaEntityNew;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonValues;

public class AgentTA_DA_Activity extends AgentActionbarBase implements
		OnClickListener, IAsynchronousTask {

	LinearLayout llParentContainer;
	ImageView ivAddCost;
	View child;
	ArrayList<LinearLayout> mViews = new ArrayList<LinearLayout>();
	// List<View> viewList = new ArrayList<View>();
	int viewChildCount = 0;
	Button btnOk;
	EditText etTourCost, etPublishDate, etFrom, etTo, etDistance, etCarName,
			etOtherCost, etTourPurpose;

	TimePicker ivStartTime, ivEndTime;
	TextView tvTotalCost;
	EditText etOthersCost;
	int totalCost = 0;

	private int year;
	private int month;
	private int day;

	private int hour;
	private int minute;
	JSONObject object;
	DownloadableAsyncTask asyncTask;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_ta_da);
		initViews();
	}

	private void initViews() {
		llParentContainer = (LinearLayout) findViewById(R.id.rlTransportationCostContainer);
		ivAddCost = (ImageView) findViewById(R.id.ivAddCost);
		tvTotalCost = (TextView) findViewById(R.id.tvTotalCost);
		etOthersCost = (EditText) findViewById(R.id.etOthersCost);
		etTourPurpose= (EditText) findViewById(R.id.etTourPurpose);

		btnOk = (Button) findViewById(R.id.btnOk);

		ivAddCost.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		ivAddCost.performClick();
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("InflateParams")
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.ivAddCost) {

			if (mViews.size() > 0
					&& (etPublishDate.getText().toString().trim().equals("")
							|| etTo.getText().toString().trim().equals("")
							|| etFrom.getText().toString().trim().equals("") || etCarName
							.getText().toString().trim().equals(""))) {
				return;

			}

			child = LayoutInflater.from(this).inflate(R.layout.ta_da_items,
					null);

			ImageView ivPublishDate = (ImageView) (child
					.findViewById(R.id.ivPublishDate));
			ivStartTime = (TimePicker) (child.findViewById(R.id.ivStartTime));
			ivEndTime = (TimePicker) (child.findViewById(R.id.ivEndTime));
			etPublishDate = (EditText) (child.findViewById(R.id.etPublishDate));

			etTourCost = (EditText) (child.findViewById(R.id.etTourCost));
			etOtherCost = (EditText) (child.findViewById(R.id.etOtherCost));
			etTourPurpose = (EditText) (child.findViewById(R.id.etTourPurpose));
			etTourCost.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

					/*
					 * //if (mViews.size() > 0) { for (int i = 1; i <
					 * mViews.size(); i++) { LinearLayout layout =
					 * mViews.get(i); for (int c = 0; c <
					 * layout.getChildCount(); c++) { View childView =
					 * layout.getChildAt(c); EditText etTourCost = (EditText)
					 * (childView .findViewById(R.id.etTourCost)); totalCost +=
					 * Integer.parseInt(etTourCost
					 * .getText().toString().trim()); } } totalCost+=
					 * Integer.parseInt(s.toString()); totalCost +=
					 * Integer.parseInt(etOthersCost.getText()
					 * .toString().trim()); tvTotalCost.setText("Total Cost : "
					 * + totalCost); }
					 */

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});

			etFrom = (EditText) (child.findViewById(R.id.etFrom));
			etTo = (EditText) (child.findViewById(R.id.etTo));
			etDistance = (EditText) (child.findViewById(R.id.etDistance));
			etCarName = (EditText) (child.findViewById(R.id.etCarName));

			ivPublishDate.setOnClickListener(new View.OnClickListener() {

				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					final Calendar c = Calendar.getInstance();
					year = c.get(Calendar.YEAR);
					month = c.get(Calendar.MONTH);
					day = c.get(Calendar.DAY_OF_MONTH);
					showDialog(1);

				}
			});

			ivStartTime.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					final Calendar c = Calendar.getInstance();
					hour = c.get(Calendar.HOUR_OF_DAY);
					minute = c.get(Calendar.MINUTE);

					ivStartTime.setCurrentHour(hour);
					ivStartTime.setCurrentMinute(minute);
					showDialog(2);
				}
			});
			llParentContainer.addView(child);
			// viewList.add(child);
			mViews.add((LinearLayout) child);
		} else if (view.getId() == R.id.btnOk) {
			
			if((etPublishDate.getText().toString().trim().equals("")
							|| etTo.getText().toString().trim().equals("")
							|| etFrom.getText().toString().trim().equals("") || etCarName
							.getText().toString().trim().equals(""))
					|| etTourPurpose.getText().toString().trim().equals("")){
				return;
						
					}

			JSONArray jsonArr = new JSONArray();
			for (LinearLayout layout : mViews) {
				for (int c = 0; c < layout.getChildCount(); c++) {
					JSONObject jsonObj = new JSONObject();

					View childView = layout.getChildAt(c);

					String TourCost, PublishDate, From, To, Distance, CarName, othersAmount, tourPurpose;

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
					EditText etOtherCost = (EditText) (child
							.findViewById(R.id.etOtherCost));
					EditText etTourPurpose = (EditText) (child
							.findViewById(R.id.etTourPurpose));

					TourCost = etTourCost.getText().toString();
					PublishDate = etPublishDate.getText().toString();
					From = etFrom.getText().toString();
					To = etTo.getText().toString();
					Distance = etDistance.getText().toString();
					CarName = etCarName.getText().toString();
					othersAmount = etOtherCost.getText().toString().trim();
					tourPurpose = etTourPurpose.getText().toString().trim();

					/*
					 * {"tada":[{"agentID":1,"date":"2015-01-01","startPlace":"abc"
					 * ,"startTime":"10.00am","endPlace":"xyz",
					 * "endTime":"12.00pm"
					 * ,"description":"dddd","vehicelName":"cng"
					 * ,"distance":4.0,"amount":220.0,"otherAmount":30.0,
					 * 
					 * "totalAmount":250.0,"status":1}]}
					 */

					jsonObj.put("agentID", CommonTasks.getPreferences(
							getApplicationContext(), CommonConstraints.USER_ID));
					jsonObj.put("date", PublishDate);
					jsonObj.put("startPlace", From);
					jsonObj.put("startTime", "10.00am");
					jsonObj.put("endPlace", To);
					jsonObj.put("endTime", "12.00am");
					jsonObj.put("description", tourPurpose);
					jsonObj.put("vehicelName", CarName);
					jsonObj.put("distance", Distance);

					jsonObj.put("amount", TourCost);
					jsonObj.put("otherAmount", othersAmount);
					jsonObj.put("totalAmount", Double.parseDouble(othersAmount)
							+ Double.parseDouble(TourCost));
					jsonObj.put("status", 1);
					jsonArr.add(jsonObj);

				}
			}
			
			
			object=new JSONObject();
			object.put("tada", jsonArr);
			Log.d("BSS", object.toJSONString());
			LoadInforamtion();

		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		case 2:
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

			// set current time into textview

			if (view.getId() == R.id.ivStartTime) {
				ivStartTime.setCurrentHour(hour);
				ivStartTime.setCurrentMinute(minute);
			} else {
				ivEndTime.setCurrentHour(hour);
				ivEndTime.setCurrentMinute(minute);
			}

		}
	};
	
	private void LoadInforamtion(){
		if(asyncTask != null)
			asyncTask.cancel(true);
		asyncTask = new DownloadableAsyncTask(this);
		asyncTask.execute();
	}

	@Override
	public void showProgressBar() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please Wait...");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	public void hideProgressBar() {
		progressDialog.dismiss();
	}

	@Override
	public Object doInBackground() {
		IAgent agent = new AgentManager();
		return agent.addTada(object);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			boolean result = (boolean) data;
			if(result){
				CommonTasks.showToast(this, "T.A.D.A Successfully Submit.");
				onBackPressed();
			}
		}
	}

}
