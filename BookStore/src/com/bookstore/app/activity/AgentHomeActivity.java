package com.bookstore.app.activity;

import com.bookstore.app.adapters.AgentViewPagerAdapter;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.base.BookStoreService;
import com.bookstore.app.utils.CommonTasks;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class AgentHomeActivity extends AgentActionbarBase implements
		TabListener {
	String[] tab = { "Completed Jobs", "Pending Jobs", "Submitted  Job",
			"Rejected Job" };
	ViewPager pager;
	AgentViewPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_home);
		initialization();

	}

	private void initialization() {
		pager = (ViewPager) findViewById(R.id.agentMasterPage);
		adapter = new AgentViewPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Adding Tabs
		for (String tab_name : tab) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (CommonTasks.checkPlayServices(this)) {
			if (!CommonTasks.checkServiceIsRunning(getApplicationContext())) {
				Log.d("BOOT", "Agent Home: Service is Starting");
				startService(new Intent(this, BookStoreService.class));
			} else {
				Log.d("BOOT",
						"Agent Home : BookStore Service is already Running");
			}
		} else {
			Log.d("BOOT", "Agent Home : Play Service Not Enabled");
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		pager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
