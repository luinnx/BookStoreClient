package com.bookstore.app.base;

import com.bookstore.app.activity.AgentListActivity;
import com.bookstore.app.activity.AgentMyProfileActivity;
import com.bookstore.app.activity.R;
import com.bookstore.app.utils.CommonTasks;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AgentActionbarBase extends FragmentActivity {
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Example);
		super.onCreate(savedInstanceState);
		createActionBar();
	}

	private void createActionBar() {
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.agent_actionbar_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_donation:
			CommonTasks.showLogs(getApplicationContext(), "Donation");
			break;
		case R.id.action_profile:
			Intent int2 = new Intent(getApplicationContext(),
					AgentMyProfileActivity.class);
			int2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(int2);
			break;
			
		}
		return true;
	}
}
