package com.bookstore.app.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.bookstore.app.adapters.DonationListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.customview.EndlessScrollListener;
import com.bookstore.app.entities.BookEntity;
import com.bookstore.app.entities.DonationEntity;
import com.bookstore.app.entities.DonationListRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;

public class DonationListActivity extends BookStoreActionBarBase implements
		OnItemClickListener, IAsynchronousTask {

	ListView lvAllDonationList;

	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	DonationListAdapter adapter;
	DonationListRoot donationListRoot;
	Dialog donationDetails;
	String whichPurpose = "FETCH_DONATION";
	DonationEntity donationEntity;
	String approvedAmount;
	int donationStatus;
	int pageIndex = 0;
	EndlessScrollListener scrollListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donation);
		initViews();
	}

	private void initViews() {
		lvAllDonationList = (ListView) findViewById(R.id.lvAllDonationList);
		lvAllDonationList.setOnItemClickListener(this);
		scrollListener = new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				whichPurpose = "download_next_donation";
				pageIndex++;
				loadInforMation();
			}
		};
		lvAllDonationList.setOnScrollListener(scrollListener);
		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		loadInforMation();

	}

	private void loadInforMation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();

	}

	@Override
	public void showProgressBar() {
		progressDialog = new ProgressDialog(this,
				ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setMessage("Please Wait...");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	public void hideProgressBar() {
		progressDialog.dismiss();
	}

	@Override
	public Object doInBackground() {
		IAdminManager manager = new AdminManager();
		return manager.getAllDonationList(pageIndex);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			donationListRoot = new DonationListRoot();
			donationListRoot = (DonationListRoot) data;
			if (whichPurpose.equals("FETCH_DONATION")) {
				if (donationListRoot != null
						&& donationListRoot.donationList.size() > 0) {
					adapter = new DonationListAdapter(getApplicationContext(),
							R.layout.book_list_item,
							donationListRoot.donationList);
					lvAllDonationList.setAdapter(adapter);
				}

			} else if (whichPurpose.equals("download_next_donation")) {
				if (donationListRoot != null
						&& donationListRoot.donationList.size() > 0) {
					for (DonationEntity donationEntity : donationListRoot.donationList) {
						adapter.add(donationEntity);
					}
					adapter.notifyDataSetChanged();
				}
			}

		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please Try again");
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		donationEntity = donationListRoot.donationList.get(position);

		Intent intent = new Intent(getApplicationContext(),
				DonationAcceptRejectActivity.class);
		intent.putExtra("DONATION_ID", "" + donationEntity.id);
		startActivity(intent);

	}

}
