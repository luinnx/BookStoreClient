package com.bookstore.app.adapters;

import com.bookstore.app.fragments.AgentCompleteJobListFragment;
import com.bookstore.app.fragments.AgentPendingJobListFragment;
import com.bookstore.app.fragments.AgentRejectedJobListFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AgentViewPagerAdapter extends FragmentPagerAdapter{

	public AgentViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:			
			return new AgentCompleteJobListFragment();
		case 1:			
			return new AgentPendingJobListFragment();
		case 2:
			return new AgentRejectedJobListFragment();
		default:
			return null;
		}
	}

	@Override
	public int getCount() {
		return 3;
	}

}
