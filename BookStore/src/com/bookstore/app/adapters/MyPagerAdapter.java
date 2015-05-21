package com.bookstore.app.adapters;

import com.bookstore.app.fragments.AgentsListLocationActivity;
import com.bookstore.app.fragments.CompletedJobsFragment;
import com.bookstore.app.fragments.PendingJobsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public class MyPagerAdapter extends FragmentPagerAdapter {
	public MyPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}
	
	@Override
	public Fragment getItem(int pos) {
		switch (pos) {
		
		
		case 0:
			return AgentsListLocationActivity
					.newInstance("AgentsListLocationActivity, Instance 1");
		case 1:
			return CompletedJobsFragment
					.newInstance("CompletedJobsFragment, Instance 2");
		case 2:
			return PendingJobsFragment
					.newInstance("PendingJobsFragment, Instance 3");

		default:
			return CompletedJobsFragment
					.newInstance("AgentsListLocationActivity, Instance 1");
		

		/*case 0:
			return new AgentsListLocationActivity();
		case 1:			
			return new AgentCompleteJobListFragment();
		case 2:			
			return new AgentPendingJobListFragment();
		default:
			return null;*/
		
		}
	}

	@Override
	public int getCount() {
		return 3;
	}
}