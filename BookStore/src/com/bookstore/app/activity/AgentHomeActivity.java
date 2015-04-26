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

public class AgentHomeActivity extends AgentActionbarBase implements TabListener {
	String[] tab = { "Completed Jobs", "Pending Jobs" };
	ViewPager pager;
	AgentViewPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_home);
		initialization();
		if(CommonTasks.checkPlayServices(this))
			startService(new Intent(this, BookStoreService.class));
		
	}

	@SuppressWarnings("deprecation")
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

}
