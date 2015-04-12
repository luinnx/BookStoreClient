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
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;

public class BookListActivity extends BookStoreActionBarBase implements
		OnItemClickListener, IAsynchronousTask {

	ListView lvAllAgentList;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog dialog;
	BookListAdapter adapter;

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
		Intent intent = new Intent(getApplicationContext(),
				IndividualBookDetailsActivity.class);
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
		return manager.getAgentList();
	}

	@Override
	public void processDataAfterDownload(Object data) {
		ArrayList<BookEntity> entities = new ArrayList<BookEntity>();
		BookEntity entity = new BookEntity();
		entity._id = 1;
		entity.full_name = "Bangla First paper";
		entity.Auther_name = "Bangla Academy";
		entity.isbn_no = "ACDSG123";
		entity.avaible = 128;
		entity.quantity = 200;
		entity.pic_url = null;
		entity.price = 200;
		entity.publisher_name = "Grontho Kutir";
		entities.add(entity);

		entity = new BookEntity();
		entity._id = 2;
		entity.full_name = "English First paper";
		entity.Auther_name = "Bangla Academy";
		entity.isbn_no = "ACDSG124";
		entity.avaible = 136;
		entity.quantity = 250;
		entity.pic_url = null;
		entity.price = 250;
		entity.publisher_name = "Grontho Kutir";
		entities.add(entity);
		adapter = new BookListAdapter(getApplicationContext(),
				R.layout.agent_list_item, entities);
		lvAllAgentList.setAdapter(adapter);
	}

}
