package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bookstore.app.adapters.AgentListAdapter;
import com.bookstore.app.adapters.TaDaListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.entities.TaDaListRoot;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

public class AgentTADAListActivity extends AgentActionbarBase implements
		OnItemClickListener, IAsynchronousTask {

	ListView lvAllTaDaList;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;
	TaDaListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_tada_list);
		initViews();
		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
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
		IAgent agent = new AgentManager();
		return agent.getAllTaDaList(CommonTasks.getPreferences(
				getApplicationContext(), CommonConstraints.USER_ID), 0);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			TaDaListRoot taDaListRoot = (TaDaListRoot) data;

			adapter = new TaDaListAdapter(getApplicationContext(),
					R.layout.agent_list_item, taDaListRoot.tadaList);
			lvAllTaDaList.setAdapter(adapter);

		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please Try again");
		}

	}

}
