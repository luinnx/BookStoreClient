package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.bookstore.app.adapters.AgentListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.customview.EndlessScrollListener;
import com.bookstore.app.entities.AgentEntity;
import com.bookstore.app.entities.AgentListRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;

public class AgentListActivity extends BookStoreActionBarBase implements
		OnItemClickListener, IAsynchronousTask, OnQueryTextListener {

	ListView lvAllAgentList;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;
	AgentListAdapter adapter;
	AgentListRoot agentListRoot=null;
	
	EndlessScrollListener scrollListener;
	int pageIndex = 0;
	String whichMode="";
	SearchView searchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_agent_list);
		initViews();
		
		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		loadInforMation();

	}

	private void initViews() {
		searchView=(SearchView) findViewById(R.id.svAgentList);
		searchView.setOnQueryTextListener(this);
		lvAllAgentList = (ListView) findViewById(R.id.lvAllAgentList);
		lvAllAgentList.setOnItemClickListener(this);
		pageIndex=0;
		
		scrollListener = new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				whichMode = "download_next_Agent";
				pageIndex++;
				loadInforMation();
			}
		};
		lvAllAgentList.setOnScrollListener(scrollListener);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		AgentEntity agentEntity=agentListRoot.agentList.get(position);
		Intent intent = new Intent(getApplicationContext(),
				IndividualAgentDetailsActivity.class);
		intent.putExtra("AGENT_ID", ""+agentEntity._id);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	private void loadInforMation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();

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
		
		if(!whichMode.equals("download_next_Agent")){
			IAdminManager manager = new AdminManager();
			return manager.getAgentList(0);
		}else{
			IAdminManager manager = new AdminManager();
			return manager.getAgentList(pageIndex);
		}
		
	}

	@Override
	public void processDataAfterDownload(Object data) {

		if (data != null) {
			
			
			if(!whichMode.equals("download_next_Agent")){
				agentListRoot = new AgentListRoot();
				agentListRoot = (AgentListRoot) data;
				adapter = new AgentListAdapter(getApplicationContext(),
						R.layout.agent_list_item, agentListRoot.agentList);
				lvAllAgentList.setAdapter(adapter);
			}else{
				agentListRoot = new AgentListRoot();
				if(agentListRoot != null && agentListRoot.agentList.size()>0){
					for (AgentEntity agentEntity : agentListRoot.agentList) {
						adapter.add(agentEntity);
					}
					adapter.notifyDataSetChanged();
				}
			}
			
		} else {
			CommonTasks.showToast(getApplicationContext(), "Internal Server  Error!!! Please Try again later");
		}
	}

	@Override
	public boolean onQueryTextChange(String query) {
		adapter.searchAgent(query);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
