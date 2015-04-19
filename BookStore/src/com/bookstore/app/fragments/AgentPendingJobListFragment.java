package com.bookstore.app.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bookstore.app.activity.AgentIndividualJobDetailsActivity;
import com.bookstore.app.activity.R;
import com.bookstore.app.adapters.AgentJobListAdapter;
import com.bookstore.app.adapters.AgentPendingJobListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.entities.AgentJobList;
import com.bookstore.app.entities.AgentJobListRoot;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

public class AgentPendingJobListFragment extends Fragment implements IAsynchronousTask, OnItemClickListener{
	ListView pending_job_list;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	AgentPendingJobListAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_agent_pending_job, container, false);
		initalization(root);
		return root;
	}

	private void initalization(ViewGroup root) {
		pending_job_list = (ListView) root.findViewById(R.id.pending_job_list);
		pending_job_list.setOnItemClickListener(this);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		LoadPendingJob();
	}

	private void LoadPendingJob() {
		if(downloadableAsyncTask != null)
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
		IAgent agent = new AgentManager();
		return agent.getJobList(Integer.parseInt(CommonTasks.getPreferences(getActivity(), CommonConstraints.USER_ID)), 0, 0);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			AgentJobListRoot agentJobListRoot = (AgentJobListRoot) data;
			if(agentJobListRoot.agentJobList!=null&& agentJobListRoot.agentJobList.size()>0){
				adapter = new AgentPendingJobListAdapter(getActivity(), R.layout.agent_job_list_item, agentJobListRoot.agentJobList);
				pending_job_list.setAdapter(adapter);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		try{
			AgentJobList agentJobList = (AgentJobList) pending_job_list.getItemAtPosition(position);
			if(agentJobList != null){
				Intent intent = new Intent(getActivity(), AgentIndividualJobDetailsActivity.class);
				intent.putExtra("JOB_ID", ""+agentJobList.JobID);
				intent.putExtra("MODE", "0");
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}
	}
}
