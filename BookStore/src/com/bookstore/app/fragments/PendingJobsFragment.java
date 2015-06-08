package com.bookstore.app.fragments;

import java.util.ArrayList;

import com.bookstore.app.activity.AdminJobAcceptRejectActivity;
import com.bookstore.app.activity.AgentIndividualJobDetailsActivity;
import com.bookstore.app.activity.IndividualJobDetailsActivity;
import com.bookstore.app.activity.R;
import com.bookstore.app.adapters.JobListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.entities.JobEntity;
import com.bookstore.app.entities.JobListRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import com.google.android.gms.internal.lv;

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

public class PendingJobsFragment extends Fragment implements IAsynchronousTask,
		OnItemClickListener {

	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	JobListAdapter adapter;
	ListView listView;
	JobListRoot jobListRoot = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_pending_jobs, container,
				false);

		listView = (ListView) v.findViewById(R.id.lvJobList);
		listView.setOnItemClickListener(this);

		return v;
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

	public static PendingJobsFragment newInstance(String text) {

		PendingJobsFragment f = new PendingJobsFragment();
		Bundle b = new Bundle();
		b.putString("msg", text);

		f.setArguments(b);

		return f;
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
		IAdminManager manager = new AdminManager();

		return manager.getJobList(CommonConstraints.PENDING_JOB, 0);
	}

	@Override
	public void processDataAfterDownload(Object data) {

		if (data != null) {
			jobListRoot = new JobListRoot();
			jobListRoot = (JobListRoot) data;
			if (jobListRoot != null && jobListRoot.jobList.size() > 0) {
				adapter = new JobListAdapter(getActivity(),
						R.layout.job_list_item, jobListRoot.jobList);
				listView.setAdapter(adapter);
			} else {
				adapter = new JobListAdapter(getActivity(),
						R.layout.job_list_item, jobListRoot.jobList);
				listView.setAdapter(adapter);
			}

		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		JobEntity jobEntity = (JobEntity) listView.getItemAtPosition(position);
		if (CommonTasks.getPreferences(getActivity(),
				CommonConstraints.USER_TYPE).equals("1")) {
			Intent intent = new Intent(getActivity(),
					IndividualJobDetailsActivity.class);
			intent.putExtra("JOB_ID", "" + jobEntity.jobid);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else {
			Intent intent = new Intent(getActivity(),
					AgentIndividualJobDetailsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}

	}

}
