package com.bookstore.app.activity;

import com.bookstore.app.adapters.MyPagerAdapter;
import com.bookstore.app.base.BookStoreActionBarBase;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class AgentHomeActivity extends BookStoreActionBarBase implements TabListener {
	String[] tab = {"Completed Jobs","Pending Jobs"};
	ActionBar actionBar;
	ViewPager pager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_home);
		
		pager = (ViewPager) findViewById(R.id.viewPager);
		
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        actionBar = getActionBar();
        this.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		this.actionBar.removeAllTabs();
		for (int i=0; i<tab.length;i++) {
			this.actionBar.addTab(this.actionBar.newTab().setText(tab[i]).setTabListener(this));
		}
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
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
		actionBar.setSelectedNavigationItem(tab.getPosition());
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

}
