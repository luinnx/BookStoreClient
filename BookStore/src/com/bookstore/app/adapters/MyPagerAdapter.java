package com.bookstore.app.adapters;

import com.bookstore.app.fragments.AdminRejectedJobListFragment;
import com.bookstore.app.fragments.AdminSubmittedJobFragment;
import com.bookstore.app.fragments.AgentsListLocationActivity;
import com.bookstore.app.fragments.CompletedJobsFragment;
import com.bookstore.app.fragments.CompletedJobsFragmentNew;
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
			return CompletedJobsFragmentNew
					.newInstance("CompletedJobsFragmentNew, Instance 2");
		case 2:
			return PendingJobsFragment
					.newInstance("PendingJobsFragment, Instance 3");
		case 3:
			return  AdminRejectedJobListFragment.newInstance("AgentRejectedJobListFragment, Instance 4");
		case 4:
		return AdminSubmittedJobFragment.newInstance("Submitted Job, Instance 5");

		default:
			return null;

			/*
			 * case 0: return new AgentsListLocationActivity(); case 1: return
			 * new AgentCompleteJobListFragment(); case 2: return new
			 * AgentPendingJobListFragment(); default: return null;
			 */

		}
	}

	@Override
	public int getCount() {
		return 5;
	}
}