package com.bookstore.app.adapters;

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
			return CompletedJobsFragment
					.newInstance("CompletedJobsFragment, Instance 1");
		case 1:
			return PendingJobsFragment
					.newInstance("PendingJobsFragment, Instance 2");

		default:
			return CompletedJobsFragment
					.newInstance("CompletedJobsFragment, Instance 1");
		}
	}

	@Override
	public int getCount() {
		return 2;
	}
}