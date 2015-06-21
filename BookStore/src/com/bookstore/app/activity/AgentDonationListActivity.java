package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bookstore.app.adapters.AgentDonationAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.customview.EndlessScrollListener;
import com.bookstore.app.entities.AgentDonationEntity;
import com.bookstore.app.entities.AgentDonationEntityRoot;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonValues;

public class AgentDonationListActivity extends AgentActionbarBase implements OnItemClickListener, IAsynchronousTask{
	
	ListView donation_list;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	EndlessScrollListener scrollListener;
	int pageIndex = 0;
	String whichMode = "";
	AgentDonationAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donation_list);
		
		initialization();
		
		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}		
		whichMode = "download_all_donation";
		pageIndex = 0;
		LoadInformation();
	}

	private void initialization() {
		donation_list = (ListView) findViewById(R.id.donation_list);
		donation_list.setOnItemClickListener(this);
		
		scrollListener = new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if (!CommonTasks.isOnline(getApplicationContext())) {
					CommonTasks.goSettingPage(getApplicationContext());
					return;
				}
				whichMode = "download_next_donation";
				pageIndex++;
				LoadInformation();
			}
		};
		donation_list.setOnScrollListener(scrollListener);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		try{
			AgentDonationEntity agentDonationEntity = (AgentDonationEntity) donation_list.getItemAtPosition(position);
			if(agentDonationEntity != null){
				Intent intent = new Intent(this, AgentDonationAcceptRejectResultActivity.class);
				intent.putExtra("DONATION_ID", ""+agentDonationEntity.id);
				intent.putExtra("DONATION_STATUS", agentDonationEntity.status);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}
	}
	
	private void LoadInformation(){
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressBar() {
		progressDialog = new ProgressDialog(this,
				ProgressDialog.THEME_HOLO_LIGHT);
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
		IAgent agent = new AgentManager();
		return agent.getAllDonation(Integer.parseInt(CommonTasks.getPreferences(this, CommonConstraints.USER_ID)), pageIndex);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			AgentDonationEntityRoot agentDonationEntityRoot = (AgentDonationEntityRoot) data;
			if(whichMode.equals("download_all_donation")){
				if(agentDonationEntityRoot != null && agentDonationEntityRoot.donationList.size()>0){
					adapter = new AgentDonationAdapter(this, R.layout.adapter_donation_item, agentDonationEntityRoot.donationList);
					donation_list.setAdapter(adapter);
				}
			}else if(whichMode.equals("download_next_donation")){
				if(agentDonationEntityRoot != null && agentDonationEntityRoot.donationList.size()>0){
					for (AgentDonationEntity agentDonationEntity : agentDonationEntityRoot.donationList) {
						adapter.add(agentDonationEntity);
					}
					adapter.notifyDataSetChanged();
				}
			}
		}
	}
}
