package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.interfaces.IAsynchronousTask;

public class AgentTADAListActivity extends AgentActionbarBase implements OnItemClickListener, IAsynchronousTask {

	ListView lvAllTaDaList;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_tada_list);
		initViews();
		loadInformation();
	}

	private void loadInformation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
		
	}

	private void initViews() {
		lvAllTaDaList = (ListView) findViewById(R.id.lvAllTaDaList);
		lvAllTaDaList.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showProgressBar() {
		dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		dialog.setMessage("Loading , Plaese wait...");
		dialog.setCancelable(false);
		dialog.show();
		
	}

	@Override
	public void hideProgressBar() {
		dialog.dismiss();
		
	}

	@Override
	public Object doInBackground() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		// TODO Auto-generated method stub
		
	}

}
