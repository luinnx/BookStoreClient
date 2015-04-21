package com.bookstore.app.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;

public class AddBookActivity extends BookStoreActionBarBase implements
		OnClickListener, IAsynchronousTask {

	Spinner spCategory, spSubCatagory, spTextBooksElements, spSubSubCatagory;
	String[] bookTypes, guideSubElements, textBookSubElements,
			thirdDergeeSubElements;
	String bookType, secondDegreeSubElement;

	ImageView ivCaptureImage;
	EditText etBookName, etWritterName, etPublisherName, etBookCondition,
			etBookQuantity, etISBNNumber, etPublishDate, etBookPrice;
	Button btnOk;
	String bookName, writterName, publisherName, bookCondition, bookQuantity,
			isbnNumber, publishDate, bookPrice;
	int category, subCatagory, subSubCatagory;

	DownloadableAsyncTask downloadAsyncTask;
	ProgressDialog dialog;
	File file;
	public byte[] selectedFile;
	public String filename = "";
	
	Uri uriSavedImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_book_test);
		initViews();
	}

	private void initViews() {

		etBookName = (EditText) findViewById(R.id.etBookName);
		etWritterName = (EditText) findViewById(R.id.etWritterName);
		etPublisherName = (EditText) findViewById(R.id.etPublisherName);
		etBookCondition = (EditText) findViewById(R.id.etBookCondition);
		etBookQuantity = (EditText) findViewById(R.id.etBookQuantity);
		etISBNNumber = (EditText) findViewById(R.id.etISBNNumber);
		etPublishDate = (EditText) findViewById(R.id.etPublishDate);
		etBookPrice = (EditText) findViewById(R.id.etBookPrice);
		ivCaptureImage = (ImageView) findViewById(R.id.ivCaptureImage);
		ivCaptureImage.setOnClickListener(this);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);

		spCategory = (Spinner) findViewById(R.id.spCategory);
		spSubCatagory = (Spinner) findViewById(R.id.spSubCatagory);
		spSubSubCatagory = (Spinner) findViewById(R.id.spSubSubCatagory);
		bookTypes = new String[] { "Guide Books", "Text Books" };
		guideSubElements = new String[] { "SSC", "HSC", "DEGREE", "HONOURS",
				"MASTERS", "TEST PAPERS" };
		textBookSubElements = new String[] { "DEGREE", "HONOURS" };
		thirdDergeeSubElements = new String[] { "1st Year", "2nd Year",
				"3rd Year" };
		spCategory.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, bookTypes));

		spCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				category = position;
				bookType = spCategory.getSelectedItem().toString();
				spSubCatagory.setVisibility(View.VISIBLE);
				if (bookType.equals("Guide Books")) {
					spSubCatagory.setAdapter(new ArrayAdapter<String>(
							AddBookActivity.this,
							android.R.layout.simple_dropdown_item_1line,
							guideSubElements));
					spSubCatagory
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int position, long arg3) {
									subCatagory = position;
									secondDegreeSubElement = spSubCatagory
											.getSelectedItem().toString();

									if (secondDegreeSubElement
											.equals("HONOURS")) {
										spSubSubCatagory
												.setVisibility(View.VISIBLE);
										spSubSubCatagory
												.setAdapter(new ArrayAdapter<String>(
														AddBookActivity.this,
														android.R.layout.simple_dropdown_item_1line,
														thirdDergeeSubElements));
										spSubSubCatagory
												.setOnItemSelectedListener(new OnItemSelectedListener() {

													@Override
													public void onItemSelected(
															AdapterView<?> arg0,
															View arg1,
															int arg2, long arg3) {
														subSubCatagory = arg2;

													}

													@Override
													public void onNothingSelected(
															AdapterView<?> arg0) {
														// TODO Auto-generated
														// method stub

													}
												});
									} else {
										subSubCatagory=11;
										spSubSubCatagory
												.setVisibility(View.GONE);
									}

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {

								}
							});
				} else {
					spSubCatagory.setAdapter(new ArrayAdapter<String>(
							AddBookActivity.this,
							android.R.layout.simple_dropdown_item_1line,
							textBookSubElements));
					spSubCatagory
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int position, long arg3) {
									subCatagory = position;
									secondDegreeSubElement = spSubCatagory
											.getSelectedItem().toString();
									if (secondDegreeSubElement
											.equals("HONOURS")) {
										spSubSubCatagory
												.setVisibility(View.VISIBLE);
										thirdDergeeSubElements = new String[] {
												"1st Year", "2nd Year",
												"3rd Year" };
										spSubSubCatagory
												.setAdapter(new ArrayAdapter<String>(
														AddBookActivity.this,
														android.R.layout.simple_dropdown_item_1line,
														thirdDergeeSubElements));
										spSubSubCatagory
												.setOnItemSelectedListener(new OnItemSelectedListener() {

													@Override
													public void onItemSelected(
															AdapterView<?> arg0,
															View arg1,
															int position,
															long arg3) {

														subSubCatagory = position;
													}

													@Override
													public void onNothingSelected(
															AdapterView<?> arg0) {
														// TODO Auto-generated
														// method stub

													}
												});
									} else {
										spSubSubCatagory
												.setVisibility(View.VISIBLE);
										thirdDergeeSubElements = new String[] { "1st Year" };
										spSubSubCatagory
												.setAdapter(new ArrayAdapter<String>(
														AddBookActivity.this,
														android.R.layout.simple_dropdown_item_1line,
														thirdDergeeSubElements));
										subSubCatagory = 1;
									}

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub

								}
							});
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnOk) {

			/*
			 * etBookName, etWritterName, etPublisherName, etBookCondition,
			 * etBookQuantity, etISBNNumber, etPublishDate, etBookPrice
			 */

			if (etBookName.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Enter book name");
				return;
			} else if (etWritterName.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Writter name");
				return;
			} else if (etPublisherName.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Enter book name");
				return;
			} else if (etBookCondition.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Enter book Condition");
				return;
			} else if (etBookQuantity.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Enter book Quantity");
				return;
			} else if (etISBNNumber.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Enter book ISBN Number");
				return;
			} else if (etPublishDate.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Enter book Publish date");
				return;
			} else if (etBookPrice.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Enter book Price");
				return;
			} else if (selectedFile == null) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Capture Image");
				return;
			}

			bookName = etBookName.getText().toString().trim();
			writterName = etWritterName.getText().toString().trim();
			publisherName = etPublisherName.getText().toString().trim();
			bookCondition = etBookCondition.getText().toString().trim();
			bookQuantity = etBookQuantity.getText().toString().trim();
			isbnNumber = etISBNNumber.getText().toString().trim();
			publishDate = etPublishDate.getText().toString().trim();
			bookPrice = etBookPrice.getText().toString().trim();
			loadInformation();

		} else if (view.getId() == R.id.ivCaptureImage) {
			getImageFromCamera();
		}
	}

	private void loadInformation() {
		if (downloadAsyncTask != null)
			downloadAsyncTask.cancel(true);
		downloadAsyncTask = new DownloadableAsyncTask(this);
		downloadAsyncTask.execute();

	}

	@Override
	public void showProgressBar() {
		dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		dialog.setMessage("Adding Agent , Plaese wait...");
		dialog.setCancelable(false);
		dialog.show();

	}

	@Override
	public void hideProgressBar() {
		dialog.dismiss();

	}

	/*
	 * String bookName, String writterName, String publisherName, String
	 * bookCondition, String bookPrice, String isbnNumber, String publishDate,
	 * byte[] pic_url, String bookQuantity, String catagoryId, String
	 * subCatagoryID, String subSubCatagoryID)
	 */

	@Override
	public Object doInBackground() {
		IAdminManager adminManager = new AdminManager();
		return adminManager.addBook(bookName, writterName, publisherName,
				bookCondition, bookPrice, isbnNumber, publishDate,
				selectedFile, bookQuantity, ""+category, ""+subCatagory,
				""+subSubCatagory);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		// TODO Auto-generated method stub

	}

	private void getImageFromCamera() {
		File image = new File(appFolderCheckandCreate(), "img" + getTimeStamp() + ".jpg");
        uriSavedImage = Uri.fromFile(image);
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 100);
	}
	
	private String appFolderCheckandCreate(){

	    String appFolderPath="";
	    File externalStorage = Environment.getExternalStorageDirectory();

	    if (externalStorage.canWrite()) 
	    {
	        appFolderPath = externalStorage.getAbsolutePath() + "/BookStore";
	        File dir = new File(appFolderPath);

	        if (!dir.exists()) 
	        {
	              dir.mkdirs();
	        }

	    }
	    else
	    {
	      CommonTasks.showToast(getApplicationContext(),"  Storage media not found or is full ! ");
	    }

	    return appFolderPath;
	}
	
	private String getTimeStamp() {

	    final long timestamp = new Date().getTime();

	    final Calendar cal = Calendar.getInstance();
	                   cal.setTimeInMillis(timestamp);

	    final String timeString = new SimpleDateFormat("HH_mm_ss_SSS").format(cal.getTime());


	    return timeString;
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent data) {
		super.onActivityResult(requestCode, responseCode, data);
		if (requestCode == 100) {
			if (responseCode == RESULT_OK) {
				Uri currImageURI = uriSavedImage;
				file = new File(currImageURI.getPath());
				//file = new File(getRealPathFromURI(currImageURI));
				filename = file.getName().replaceAll("[-+^:,]", "")
						.replace(" ", "");
				Bitmap b = decodeImage(file);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				if (b.getByteCount() > (1024 * 1024)) {
					b.compress(Bitmap.CompressFormat.JPEG, 20, stream);
				}
				if (b.getByteCount() > (1024 * 512)) {
					b.compress(Bitmap.CompressFormat.JPEG, 40, stream);
				}
				if (b.getByteCount() > (1024 * 256)) {
					b.compress(Bitmap.CompressFormat.JPEG, 60, stream);
				} else {
					b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				}
				selectedFile = stream.toByteArray();
				ivCaptureImage.setImageBitmap(b);
				try{
					if (file.exists()) {
					    if (file.delete()) {
					        System.out.println("file Deleted :" + file);
					    } else {
					        System.out.println("file not Deleted :" + file);
					    }
					    sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
					    		 Uri.parse("file://" +  Environment.getExternalStorageDirectory())));
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
			}
		}
	}

	public static byte[] convertInputStreamToByteArray(InputStream input)
			throws IOException {
		byte[] buffer = new byte[8192];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		return output.toByteArray();
	}

	public String getRealPathFromURI(Uri contentUri) {

		Cursor cursor = getContentResolver()
				.query(contentUri,
						new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
						null, null, null);

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public static Bitmap decodeImage(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
