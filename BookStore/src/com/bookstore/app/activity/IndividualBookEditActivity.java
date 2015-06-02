package com.bookstore.app.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

public class IndividualBookEditActivity extends BookStoreActionBarBase implements OnClickListener, IAsynchronousTask{
	
	CircularImageView ivBookImage;
	TextView tvBookName, tvAddress, tvCurrentLocation, tvISBNNumber,
			tvPublishDate,
			tvPublisherName, tvAuthorName;
	EditText etBookQuantity, etBookPrice, etAvailable;
	Button btnOk;
	Bitmap bitmap;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	int bookID;
	String whichMoode = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_edit);
		initViews();
	}
	
	private void initViews() {
		Bundle bundle = getIntent().getExtras();
		bookID = bundle.getInt("BOOK_ID");

		tvBookName = (TextView) findViewById(R.id.tvBookName);
		tvISBNNumber = (TextView) findViewById(R.id.tvISBNNumber);
		tvPublisherName = (TextView) findViewById(R.id.tvPublisherName);
		etBookQuantity = (EditText) findViewById(R.id.etBookQuantity);
		etAvailable = (EditText) findViewById(R.id.etAvailable);
		tvPublishDate = (TextView) findViewById(R.id.tvPublishDate);
		etBookPrice = (EditText) findViewById(R.id.etBookPrice);
		tvAuthorName = (TextView) findViewById(R.id.tvAuthorName);
		ivBookImage=(CircularImageView) findViewById(R.id.ivBookImage);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);
		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		whichMoode = "book_details";
		loadInformation();
	}

	private void loadInformation() {
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
		if(whichMoode.equals("book_details")){
			BookEntity bookEntity=new BookEntity();
			bookEntity=adminManager.getIndividualBookDetails("" + bookID);
			bitmap = CommonTasks.getBitMapFromUrl(CommonUrls
					.getInstance().IMAGE_BASE_URL
					+ bookEntity.pic_url);
			return bookEntity;
		}else{
			return adminManager.bookEdit(Integer.parseInt(etBookQuantity.getText().toString()), 
					Integer.parseInt(etAvailable.getText().toString()),
					Double.parseDouble(etBookPrice.getText().toString()), 
					bookID);
		}
		
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if(whichMoode.equals("book_details")){
				BookEntity bookEntity = new BookEntity();
				bookEntity = (BookEntity) data;
				tvBookName.setText(bookEntity.full_name);
				tvISBNNumber.setText(bookEntity.isbn_no);
				tvPublisherName.setText(bookEntity.publisher_name);
				etBookQuantity.setText("" + bookEntity.quantity);
				etAvailable.setText("" + bookEntity.avaible);
				tvPublishDate.setText(bookEntity.publish_date);
				tvAuthorName.setText(bookEntity.auther_name);
				etBookPrice.setText("" + bookEntity.price);
				ivBookImage.setImageBitmap(bitmap);
			}else{
				CommonTasks.showToast(getApplicationContext(),
						"Book successfully update!");
				onBackPressed();
			}
			
		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please try again later");

		}
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnOk){
			whichMoode = "book_edit";
			loadInformation();
		}
	}

}
