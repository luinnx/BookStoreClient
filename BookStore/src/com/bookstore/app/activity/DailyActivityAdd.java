package com.bookstore.app.activity;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.entities.ResponseEntity;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

public class DailyActivityAdd extends AgentActionbarBase implements OnClickListener, IAsynchronousTask{
	EditText etTeacherName ,etInstitudeName,etFaculty,etDistrict,etMobileNumber,
	etBookName,etQuantity,etActivityDate;
	ImageView ivActivityDate;
	Button btnSubmit;
	
	private int year;
	private int month;
	private int day;
	DatePickerDialog datePickerDialog;
	
	DownloadableAsyncTask asyncTask;
	ProgressDialog progressDialog;
	JSONObject jsonObject=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_activity);
		initViews();
	}
	
	private void initViews() {
		etTeacherName=(EditText) findViewById(R.id.etTeacherName);
		etInstitudeName=(EditText) findViewById(R.id.etInstitudeName);
		etFaculty=(EditText) findViewById(R.id.etFaculty);
		etDistrict=(EditText) findViewById(R.id.etDistrict);
		etMobileNumber=(EditText) findViewById(R.id.etMobileNumber);
		etBookName=(EditText) findViewById(R.id.etBookName);
		etQuantity=(EditText) findViewById(R.id.etQuantity);
		etActivityDate=(EditText) findViewById(R.id.etActivityDate);
		
		ivActivityDate=(ImageView) findViewById(R.id.ivActivityDate);
		btnSubmit=(Button) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		ivActivityDate.setOnClickListener(this);
		
	}

	

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View arg0) {
		
		if(arg0.getId()==R.id.ivActivityDate){
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			showDialog(1);
		}else{
		
		if(etTeacherName.getText().toString().trim().equals("")){
			CommonTasks.showToast(getApplicationContext(), "Enter Teacher name");
			return;
		}else if(etInstitudeName.getText().toString().trim().equals("")){
			CommonTasks.showToast(getApplicationContext(), "Enter Institude Name");
			return;
		}else if(etFaculty.getText().toString().trim().equals("")){
			CommonTasks.showToast(getApplicationContext(), "Enter Faculty Name");
			return;
		}else if(etDistrict.getText().toString().trim().equals("")){
			CommonTasks.showToast(getApplicationContext(), "Enter District");
			return;
		}else if(etMobileNumber.getText().toString().trim().equals("")){
			CommonTasks.showToast(getApplicationContext(), "Enter Mobile Number");
			return;
		}else if(etBookName.getText().toString().trim().equals("")){
			CommonTasks.showToast(getApplicationContext(), "Enter Book Name");
			return;
		}else if(etQuantity.getText().toString().trim().equals("")){
			CommonTasks.showToast(getApplicationContext(), "Enter Book Quantity");
			return;
		}else if(etActivityDate.getText().toString().trim().equals("")){
			CommonTasks.showToast(getApplicationContext(), "Enter Date");
			return;
		}
		
		createJsonAndSendToServer();
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

			etActivityDate.setText(new StringBuilder().append(year).append("-")
					.append(month + 1).append("-").append(day).append(" "));

		}
	};
	
	private void LoadInforamtion(){
		if(asyncTask != null)
			asyncTask.cancel(true);
		asyncTask = new DownloadableAsyncTask(this);
		asyncTask.execute();
	}

	private void createJsonAndSendToServer() {
		try{
			jsonObject=new JSONObject();
			
			jsonObject.put("user_id", CommonTasks.getPreferences(getApplicationContext(), CommonConstraints.USER_ID));
			jsonObject.put("activity_date", etActivityDate.getText().toString().trim());
			jsonObject.put("teacherName", etTeacherName.getText().toString().trim());
			jsonObject.put("institute_name", etInstitudeName.getText().toString().trim());
			jsonObject.put("inititute_address", etInstitudeName.getText().toString().trim());
			jsonObject.put("book_name", etBookName.getText().toString().trim());
			jsonObject.put("book_quantity", etQuantity.getText().toString().trim());
			jsonObject.put("teacher_mobile_number", etMobileNumber.getText().toString().trim());
			jsonObject.put("district", etDistrict.getText().toString().trim());
			jsonObject.put("group_name", etFaculty.getText().toString().trim());
			
			LoadInforamtion();
		}catch(Exception exception){
			Log.d("BSS", exception.getMessage()==null?"":exception.getMessage());
		}
		
		
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
		IAgent agent=new AgentManager();
		return agent.addDailyActivity(jsonObject);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data!=null){
			ResponseEntity entity=(ResponseEntity) data;
			if(entity.status){
				CommonTasks.showToast(getApplicationContext(), entity.message);
				super.onBackPressed();
			}else{
				CommonTasks.showToast(getApplicationContext(), entity.message);
			}
		}else{
			CommonTasks.showToast(getApplicationContext(), "Critical Exception Occured. Please Try again later");
		}
		
	}


}
