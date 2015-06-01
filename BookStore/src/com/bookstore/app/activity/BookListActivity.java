package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.bookstore.app.adapters.BookListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.BookEntity;
import com.bookstore.app.entities.BookListRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;

public class BookListActivity extends BookStoreActionBarBase implements
		OnItemClickListener, IAsynchronousTask, OnItemLongClickListener {

	ListView lvAllAgentList;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	BookListAdapter adapter;
	BookListRoot bookListRoot=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_book_list);
		initViews();
		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		loadInforMation();

	}

	private void initViews() {
		lvAllAgentList = (ListView) findViewById(R.id.lvAllBookList);
		lvAllAgentList.setOnItemClickListener(this);
		lvAllAgentList.setOnItemLongClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		BookEntity bookEntity=new BookEntity();
		bookEntity=bookListRoot.bookList.get(position);
		Intent intent = new Intent(getApplicationContext(),
				IndividualBookDetailsActivity.class);
		intent.putExtra("BOOK_ID", bookEntity._id);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	private void loadInforMation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();

	}

	@Override
	public void showProgressBar() {
		progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Please Wait.");
		progressDialog.show();

	}

	@Override
	public void hideProgressBar() {
		progressDialog.dismiss();

	}

	@Override
	public Object doInBackground() {
		IAdminManager manager = new AdminManager();
		return manager.getBookList(0);
	}

	@Override
	public void processDataAfterDownload(Object data) {

		if (data != null) {
			bookListRoot = new BookListRoot();
			bookListRoot = (BookListRoot) data;
			adapter = new BookListAdapter(getApplicationContext(),
					R.layout.book_list_item, bookListRoot.bookList);
			lvAllAgentList.setAdapter(adapter);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		BookEntity bookEntity=new BookEntity();
		bookEntity=bookListRoot.bookList.get(position);
		Intent intent = new Intent(getApplicationContext(),
				IndividualBookEditActivity.class);
		intent.putExtra("BOOK_ID", bookEntity._id);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		return true;
	}

}
