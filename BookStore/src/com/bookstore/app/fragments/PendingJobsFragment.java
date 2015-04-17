package com.bookstore.app.fragments;

import java.util.ArrayList;

import com.bookstore.app.activity.AgentIndividualJobDetailsActivity;
import com.bookstore.app.activity.IndividualJobDetailsActivity;
import com.bookstore.app.activity.R;
import com.bookstore.app.adapters.JobListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.entities.JobEntity;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
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

public class PendingJobsFragment extends Fragment implements IAsynchronousTask, OnItemClickListener{

	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;
	JobListAdapter adapter;
	ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pending_jobs, container, false);

      listView=(ListView) v.findViewById(R.id.lvJobList);
      listView.setOnItemClickListener(this);
        

        return v;
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
    	loadInformation();
    }
    
    public void loadInformation(){
    	if(downloadableAsyncTask!=null)
    		downloadableAsyncTask.cancel(true);
    	downloadableAsyncTask=new DownloadableAsyncTask(this);
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
		IAdminManager manager=new AdminManager();
		
		return manager.getJobList(CommonConstraints.COMPLETED_JOB);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		ArrayList<JobEntity> list=new ArrayList<JobEntity>();
		JobEntity entity=new JobEntity();
		entity.agentname="Sumit saha";
		entity.bookname="Higher Math";
		entity.agentaddress="Savar";
		entity.bookImage="";
		entity.teacherinstitute="Savar";
		entity.quantity=13;
		list.add(entity);
		adapter=new JobListAdapter(getActivity(), R.layout.job_list_item, list);
		listView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(CommonTasks.getPreferences(getActivity(), CommonConstraints.USER_TYPE).equals("1")){
			Intent intent=new Intent(getActivity(), IndividualJobDetailsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else{
			Intent intent=new Intent(getActivity(), AgentIndividualJobDetailsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
	}

	
}
