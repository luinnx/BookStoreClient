package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.bookstore.app.adapters.TeacherListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.TeacherListRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;

public class TeacherListActivity extends BookStoreActionBarBase implements
		IAsynchronousTask, OnQueryTextListener {

	ListView lvAllTeacherList;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	TeacherListAdapter adapter;
	SearchView searchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_teacher_list);
		initViews();

		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		loadInforMation();

	}

	private void initViews() {
		lvAllTeacherList = (ListView) findViewById(R.id.lvAllTeacherList);
		searchView=(SearchView) findViewById(R.id.svTeacherList);
		
		searchView.setOnQueryTextListener(this);

	}

	private void loadInforMation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();

	}

	@Override
	public void showProgressBar() {
		progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Please Wait.");
		progressDialog.show();

	}

	@Override
	public void hideProgressBar() {
		progressDialog.dismiss();

	}

	@Override
	public Object doInBackground() {
		IAdminManager manager = new AdminManager();
		return manager.getTeacherList();
	}

	@Override
	public void processDataAfterDownload(Object data) {

		if (data != null) {
			TeacherListRoot teacherListRoot = new TeacherListRoot();
			teacherListRoot = (TeacherListRoot) data;
			adapter = new TeacherListAdapter(getApplicationContext(),
					R.layout.teacher_list_item, teacherListRoot.teacherList);
			lvAllTeacherList.setAdapter(adapter);
		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please Try again later");
		}

	}

	@Override
	public boolean onQueryTextChange(String charText) {
		adapter.searchTeacher(charText);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
