package com.bookstore.app.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import com.bookstore.app.adapters.TeacherListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.TeacherEntity;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;

public class TeacherListActivity extends BookStoreActionBarBase implements
		IAsynchronousTask {

	ListView lvAllTeacherList;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;
	TeacherListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_teacher_list);
		initViews();
		loadInforMation();

	}

	private void initViews() {
		lvAllTeacherList = (ListView) findViewById(R.id.lvAllTeacherList);

	}

	private void loadInforMation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();

	}

	@Override
	public void showProgressBar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideProgressBar() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object doInBackground() {
		IAdminManager manager = new AdminManager();
		return manager.getAgentList();
	}

	@Override
	public void processDataAfterDownload(Object data) {
		ArrayList<TeacherEntity> entities = new ArrayList<TeacherEntity>();
		TeacherEntity entity = new TeacherEntity();
		entity._id = 1;
		entity.full_name = "Anisur Rahman";
		entity.institute = "Alimuddin Degree Colliage";
		entity.mobile_no = "01739856984";
		entities.add(entity);

		entity = new TeacherEntity();
		entity._id = 1;
		entity.full_name = "Shariful Islam";
		entity.institute = "Nilphamari Degree Colliage";
		entity.mobile_no = "0173966984";
		entities.add(entity);
		adapter = new TeacherListAdapter(getApplicationContext(),
				R.layout.teacher_list_item, entities);
		lvAllTeacherList.setAdapter(adapter);
	}

}
