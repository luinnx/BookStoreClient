package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.BookEntity;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonUrls;
import com.mikhaellopez.circularimageview.CircularImageView;

public class IndividualBookDetailsActivity extends BookStoreActionBarBase
		implements OnClickListener, IAsynchronousTask {

	CircularImageView ivBookImage;
	TextView tvBookName, tvAddress, tvCurrentLocation, tvISBNNumber,
			tvBookQuantity, tvAvailable, tvPublishDate, tvBookCondition,
			tvBookPrice, tvPublisherName, tvAuthorName;
	Button btnOk;
	Bitmap bitmap;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	int bookID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_details);
		initViews();
	}

	private void initViews() {
		Bundle bundle = getIntent().getExtras();
		bookID = bundle.getInt("BOOK_ID");

		tvBookName = (TextView) findViewById(R.id.tvBookName);
		tvISBNNumber = (TextView) findViewById(R.id.tvISBNNumber);
		tvPublisherName = (TextView) findViewById(R.id.tvPublisherName);
		tvBookQuantity = (TextView) findViewById(R.id.tvBookQuantity);
		tvAvailable = (TextView) findViewById(R.id.tvAvailable);
		tvPublishDate = (TextView) findViewById(R.id.tvPublishDate);
		tvBookCondition = (TextView) findViewById(R.id.tvBookCondition);
		tvBookPrice = (TextView) findViewById(R.id.tvBookPrice);
		tvAuthorName = (TextView) findViewById(R.id.tvAuthorName);
		ivBookImage=(CircularImageView) findViewById(R.id.ivBookImage);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);
		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		loadInformation();
	}

	@Override
	public void onClick(View arg0) {
		super.onBackPressed();

	}

	public void loadInformation() {
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
		IAdminManager adminManager = new AdminManager();
		BookEntity bookEntity=new BookEntity();
		bookEntity=adminManager.getIndividualBookDetails("" + bookID);
		bitmap = CommonTasks.getBitMapFromUrl(CommonUrls
				.getInstance().IMAGE_BASE_URL
				+ bookEntity.pic_url);
		if(bitmap==null){
			bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_book_64);
		}
		return bookEntity;
		
		
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			BookEntity bookEntity = new BookEntity();
			bookEntity = (BookEntity) data;
			tvBookName.setText("Book Name : "+bookEntity.full_name);
			tvISBNNumber.setText(bookEntity.isbn_no);
			tvPublisherName.setText(bookEntity.publisher_name);
			tvBookQuantity.setText("" + bookEntity.quantity);
			tvAvailable.setText("" + bookEntity.avaible);
			tvPublishDate.setText(bookEntity.publish_date);
			tvBookCondition.setText(bookEntity.condition);
			tvAuthorName.setText(bookEntity.auther_name);
			tvBookPrice.setText("" + bookEntity.price);
			ivBookImage.setImageBitmap(bitmap);
			
		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please try again later");

		}

	}
}
