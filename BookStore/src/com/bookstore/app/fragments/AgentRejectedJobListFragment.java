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
import com.bookstore.app.activity.IndividualJobDetailsActivity;
import com.bookstore.app.activity.R;
import com.bookstore.app.adapters.AgentPendingJobListAdapter;
import com.bookstore.app.adapters.AgentRejectedJobListAdapter;
import com.bookstore.app.adapters.JobListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.customview.EndlessScrollListener;
import com.bookstore.app.entities.AgentJobList;
import com.bookstore.app.entities.AgentJobListRoot;
import com.bookstore.app.entities.JobEntity;
import com.bookstore.app.entities.JobListRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonValues;

public class AgentRejectedJobListFragment extends Fragment implements
		OnItemClickListener, IAsynchronousTask {
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	AgentRejectedJobListAdapter adapter;
	ListView lvJobList;
	JobListRoot jobListRoot = null;
	EndlessScrollListener scrollListener;
	int pageIndex = 0;
	String whichMode = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_agent_rejected_jobs, container, false);
		initalization(root);
		return root;
	}

	private void initalization(ViewGroup root) {
		lvJobList = (ListView) root.findViewById(R.id.lvJobList);
		lvJobList.setOnItemClickListener(this);
		
		scrollListener = new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if (!CommonTasks.isOnline(getActivity())) {
					CommonTasks.goSettingPage(getActivity());
					return;
				}
				whichMode = "download_next_job";
				pageIndex++;
				loadInformation();
			}
		};
		lvJobList.setOnScrollListener(scrollListener);
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
			whichMode = "download_all_job";
			pageIndex=0;
			loadInformation();
		} else {

		}
	}

	public void loadInformation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		try {
			AgentJobList agentJobList = (AgentJobList) lvJobList
					.getItemAtPosition(position);
			if (agentJobList != null) {
				Intent intent = new Intent(getActivity(),
						AgentIndividualJobDetailsActivity.class);
				intent.putExtra("JOB_ID", "" + agentJobList.JobID);
				intent.putExtra("MODE", "4");
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		} catch (Exception ex) {
			Log.e("BS", ex.getMessage());
		}
	}

	@Override
	public void showProgressBar() {
		progressDialog = new ProgressDialog(getActivity(),
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
		return agent.getJobList(Integer.parseInt(CommonTasks.getPreferences(
				getActivity(), CommonConstraints.USER_ID)), 4, pageIndex);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			AgentJobListRoot agentJobListRoot = (AgentJobListRoot) data;
			if(whichMode.equals("download_all_job")){
				if (agentJobListRoot.agentJobList != null && agentJobListRoot.agentJobList.size() > 0) {
					adapter = new AgentRejectedJobListAdapter(getActivity(), R.layout.agent_job_list_item, agentJobListRoot.agentJobList);
					lvJobList.setAdapter(adapter);
				} else {
					adapter = new AgentRejectedJobListAdapter(getActivity(), R.layout.agent_job_list_item, agentJobListRoot.agentJobList);
					lvJobList.setAdapter(adapter);
				}
			}else if(whichMode.equals("download_next_job")){
				if (agentJobListRoot.agentJobList != null && agentJobListRoot.agentJobList.size() > 0) {
					for (AgentJobList jobList : agentJobListRoot.agentJobList) {
						adapter.add(jobList);
					}
					adapter.notifyDataSetChanged();
				}
			}
			
		}

	}

}
