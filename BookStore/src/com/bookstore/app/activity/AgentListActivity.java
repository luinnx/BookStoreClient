package com.bookstore.app.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bookstore.app.adapters.AgentListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.AgentEntity;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;

public class AgentListActivity extends BookStoreActionBarBase implements
		OnItemClickListener, IAsynchronousTask {

	ListView lvAllAgentList;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;
	AgentListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_agent_list);
		initViews();
		loadInforMation();

	}

	private void initViews() {
		lvAllAgentList = (ListView) findViewById(R.id.lvAllAgentList);
		lvAllAgentList.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		Intent intent = new Intent(getApplicationContext(),
				IndividualAgentDetailsActivity.class);
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
		ArrayList<AgentEntity> entities = new ArrayList<AgentEntity>();
		AgentEntity entity = new AgentEntity();
		entity._id = 1;
		entity.address = "Nilphamari";
		entity.create_date = "20 jan , 2015";
		entity.email = "mesukcse08@gmail.com";
		entity.full_name = "Md. Sajedul Karim";
		entity.gcm_id = "12354875wdhwhdgjwh";
		entity.isActive = 1;
		entity.mobile_no = "01737186095";
		entity.mpo_no = "kutir123";
		entity.pic_url = "";
		entities.add(entity);

		entity = new AgentEntity();
		entity._id = 2;
		entity.address = "Jamal Pur";
		entity.create_date = "20 Dec , 2014";
		entity.email = "mhasan@gmail.com";
		entity.full_name = "Md. Mahbub hasan";
		entity.gcm_id = "1jjghs4875wdhwhdgjwh";
		entity.isActive = 1;
		entity.mobile_no = "01937569856";
		entity.mpo_no = "kutir124";
		entity.pic_url = "";
		entities.add(entity);
		adapter = new AgentListAdapter(getApplicationContext(),
				R.layout.agent_list_item, entities);
		lvAllAgentList.setAdapter(adapter);
	}

}
