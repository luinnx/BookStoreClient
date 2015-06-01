package com.bookstore.app.base;

import com.bookstore.app.activity.AddAgentActivity;
import com.bookstore.app.activity.AddBookActivity;
import com.bookstore.app.activity.AddIMEIActivity;
import com.bookstore.app.activity.AddTeacherActivity;
import com.bookstore.app.activity.AdminMapActivity;
import com.bookstore.app.activity.AdminTADAListActivity;
import com.bookstore.app.activity.AgentListActivity;
import com.bookstore.app.activity.BookListActivity;
import com.bookstore.app.activity.CreateJobActivity;
import com.bookstore.app.activity.DonationListActivity;
import com.bookstore.app.activity.LoginActivity;
import com.bookstore.app.activity.R;
import com.bookstore.app.activity.TeacherListActivity;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BookStoreActionBarBase extends FragmentActivity {

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
		inflater.inflate(R.menu.action_bar_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_agent:
			CommonTasks.showLogs(getApplicationContext(), "add agent");
			Intent intent = new Intent(getApplicationContext(),
					AddAgentActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			startActivity(intent);
			break;
		case R.id.action_add_book:
			CommonTasks.showLogs(getApplicationContext(), "add book");
			Intent int1 = new Intent(getApplicationContext(),
					AddBookActivity.class);
			int1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			startActivity(int1);
			break;
		case R.id.action_add_teacher:
			CommonTasks.showLogs(getApplicationContext(), "add teacher");
			Intent intt = new Intent(getApplicationContext(),
					AddTeacherActivity.class);
			intt.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			startActivity(intt);
			break;
			
		case R.id.action_add_imei:
			CommonTasks.showLogs(getApplicationContext(), "add teacher");
			Intent intt2 = new Intent(getApplicationContext(),
					AddIMEIActivity.class);
			intt2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			startActivity(intt2);
			break;
		case R.id.action_Agent_list:
			CommonTasks.showLogs(getApplicationContext(), "Agent List");
			Intent int2 = new Intent(getApplicationContext(),
					AgentListActivity.class);
			int2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			startActivity(int2);
			break;
		case R.id.action_book_list:
			CommonTasks.showLogs(getApplicationContext(), "Book List");
			Intent int3 = new Intent(getApplicationContext(),
					BookListActivity.class);
			int3.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			startActivity(int3);
			break;
		case R.id.action_teacher_list:
			CommonTasks.showLogs(getApplicationContext(), "Teacher List");
			Intent int4 = new Intent(getApplicationContext(),
					TeacherListActivity.class);
			int4.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			startActivity(int4);
			break;
		case R.id.action_create_job:
			Intent int5 = new Intent(getApplicationContext(),
					CreateJobActivity.class);
			int5.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			startActivity(int5);
			break;
		case R.id.action_tada_list:
			Intent tadaIntent = new Intent(getApplicationContext(),
					AdminTADAListActivity.class);
			tadaIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			startActivity(tadaIntent);
			break;
			
		case R.id.action_donation:
			Intent int6 = new Intent(getApplicationContext(),
					DonationListActivity.class);
			int6.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			startActivity(int6);
			break;

		case R.id.actionLocation:
			Intent int7 = new Intent(getApplicationContext(),
					AdminMapActivity.class);
			int7.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			startActivity(int7);
			break;
		case R.id.action_ContactUs:
			String url = "http://www.dik-grantha.com/contact.php";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
			
			break;
		case R.id.action_logout:
			CommonTasks.savePreferencesForReasonCode(this,
					CommonConstraints.USER_ID, "" + "");
			CommonTasks.savePreferencesForReasonCode(this,
					CommonConstraints.USER_TYPE, "" + "");
			CommonTasks.savePreferencesForReasonCode(this,
					CommonConstraints.GCMID, "" + "");
			Intent intent2=new Intent(getApplicationContext(),LoginActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			startActivity(intent2);
			break;
		default:
			break;
		}
		return true;
	}

}
