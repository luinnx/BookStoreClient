package com.bookstore.app.fragments;

import com.bookstore.app.activity.ActivityDetails;
import com.bookstore.app.activity.R;
import com.bookstore.app.adapters.ActivityListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.entities.ActivityEntity;
import com.bookstore.app.entities.ActivityEntityRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AgentCompleteJobListFragmentNew extends Fragment implements
		IAsynchronousTask, OnItemClickListener {

	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	ActivityListAdapter adapter;
	ListView listView;
	ActivityEntityRoot root = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_completed_jobs, container,
				false);

		listView = (ListView) v.findViewById(R.id.lvJobList);
		listView.setOnItemClickListener(this);

		return v;
	}

	public static AgentCompleteJobListFragmentNew newInstance(String text) {

		AgentCompleteJobListFragmentNew f = new AgentCompleteJobListFragmentNew();
		Bundle b = new Bundle();
		b.putString("msg", text);

		f.setArguments(b);

		return f;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (!CommonTasks.isOnline(getActivity())) {
				CommonTasks.goSettingPage(getActivity());
				return;
			}
			loadInformation();
		} else {
			
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void loadInformation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressBar() {
		progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
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
		IAgent manager = new AgentManager();

		return manager.getAllActivity(CommonTasks.getPreferences(getActivity(), CommonConstraints.USER_ID));
	}

	@Override
	public void processDataAfterDownload(Object data) {

		if (data != null) {
			root = new ActivityEntityRoot();
			root = (ActivityEntityRoot) data;
			if(root != null && root.activityList.size()>0){
				adapter = new ActivityListAdapter(getActivity(), R.layout.job_list_item,
						root.activityList);
				listView.setAdapter(adapter);
			}else{
				adapter = new ActivityListAdapter(getActivity(), R.layout.job_list_item,
						root.activityList);
				listView.setAdapter(adapter);
			}
			
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
	ActivityEntity activityEntity=(ActivityEntity) listView.getItemAtPosition(position);
	
	Intent intent=new Intent(getActivity(), ActivityDetails.class);
	intent.putExtra("ACTIVITY", activityEntity);
	startActivity(intent);

	}

}
