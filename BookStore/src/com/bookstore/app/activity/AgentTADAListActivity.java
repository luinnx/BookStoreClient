package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bookstore.app.adapters.AgentListAdapter;
import com.bookstore.app.adapters.TaDaListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.customview.EndlessScrollListener;
import com.bookstore.app.entities.AgentJobList;
import com.bookstore.app.entities.TaDaListRoot;
import com.bookstore.app.entities.TadaListEntity;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonValues;

public class AgentTADAListActivity extends AgentActionbarBase implements
		OnItemClickListener, IAsynchronousTask {

	ListView lvAllTaDaList;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;
	TaDaListAdapter adapter;
	TaDaListRoot taDaListRoot;
	EndlessScrollListener scrollListener;
	int pageIndex = 0;
	String whichMode = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_tada_list);
		initViews();
		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		whichMode = "download_all_tada";
		pageIndex=0;
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
		
		scrollListener = new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if (!CommonTasks.isOnline(AgentTADAListActivity.this)) {
					CommonTasks.goSettingPage(AgentTADAListActivity.this);
					return;
				}
				whichMode = "download_next_tada";
				pageIndex++;
				loadInformation();
			}
		};
		lvAllTaDaList.setOnScrollListener(scrollListener);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		try{
			TadaListEntity tada = (TadaListEntity) lvAllTaDaList.getItemAtPosition(arg2);
			if(tada != null){
				Intent intent = new Intent(this, AgentTADAResultActivity.class);
				intent.putExtra("TADA_ID", "" + tada.id);
				startActivity(intent);
			}			
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}

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
				getApplicationContext(), CommonConstraints.USER_ID), pageIndex);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			taDaListRoot = (TaDaListRoot) data;
			if(whichMode.equals("download_all_tada")){
				if(taDaListRoot != null && taDaListRoot.tadaList.size()>0){
					adapter = new TaDaListAdapter(getApplicationContext(),
							R.layout.tada_list_item, taDaListRoot.tadaList);
					lvAllTaDaList.setAdapter(adapter);
				}				
			}else if(whichMode.equals("download_next_tada")){
				if(taDaListRoot != null && taDaListRoot.tadaList.size()>0){
					for (TadaListEntity tadaList : taDaListRoot.tadaList) {
						adapter.add(tadaList);
					}
					adapter.notifyDataSetChanged();
				}
			}

		} else {
			/*CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please Try again");*/
		}

	}

}
