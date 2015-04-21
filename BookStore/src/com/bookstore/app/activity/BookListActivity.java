package com.bookstore.app.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bookstore.app.adapters.BookListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.BookEntity;
import com.bookstore.app.entities.BookListRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;

public class BookListActivity extends BookStoreActionBarBase implements
		OnItemClickListener, IAsynchronousTask {

	ListView lvAllAgentList;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;
	BookListAdapter adapter;
	BookListRoot bookListRoot=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_book_list);
		initViews();
		loadInforMation();

	}

	private void initViews() {
		lvAllAgentList = (ListView) findViewById(R.id.lvAllBookList);
		lvAllAgentList.setOnItemClickListener(this);

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
		// TODO Auto-generated method stub

	}

	@Override
	public void hideProgressBar() {
		// TODO Auto-generated method stub

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

}
