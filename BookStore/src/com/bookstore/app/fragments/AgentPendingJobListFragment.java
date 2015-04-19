package com.bookstore.app.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bookstore.app.activity.R;
import com.bookstore.app.adapters.AgentJobListAdapter;
import com.bookstore.app.adapters.AgentPendingJobListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.entities.AgentJobListRoot;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

public class AgentPendingJobListFragment extends Fragment implements IAsynchronousTask{
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
}
