package com.bookstore.app.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.utils.CommonTasks;

public class AddBookActivity extends BookStoreActionBarBase implements
		OnClickListener, IAsynchronousTask {

	Spinner spBookTypes, spBooksTypesElements, spTextBooksElements,
			spThirdDegreeElements;
	String[] bookTypes, guideSubElements, textBookSubElements,
			thirdDergeeSubElements;
	String bookType, secondDegreeSubElement;

	ImageView ivCaptureImage;
	EditText etBookName, etWritterName, etPublisherName, etBookCondition,
			etBookQuantity, etISBNNumber, etPublishDate, etBookPrice;
	Button btnOk;
	
	DownloadableAsyncTask downloadAsyncTask;
	ProgressDialog dialog;
	File file;
	public byte[] selectedFile;
	public String filename = "";

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

		spBookTypes = (Spinner) findViewById(R.id.spBookTypes);
		spBooksTypesElements = (Spinner) findViewById(R.id.spBooksTypesElements);
		spThirdDegreeElements = (Spinner) findViewById(R.id.spThirdDegreeElements);
		bookTypes = new String[] { "Guide Books", "Text Books" };
		guideSubElements = new String[] { "SSC", "HSC", "DEGREE", "HONOURS",
				"MASTERS", "TEST PAPERS" };
		textBookSubElements = new String[] { "DEGREE", "HONOURS" };
		thirdDergeeSubElements = new String[] { "1st Year", "2nd Year",
				"3rd Year" };
		spBookTypes.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, bookTypes));

		spBookTypes.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				bookType = spBookTypes.getSelectedItem().toString();
				spBooksTypesElements.setVisibility(View.VISIBLE);
				if (bookType.equals("Guide Books")) {
					spBooksTypesElements.setAdapter(new ArrayAdapter<String>(
							AddBookActivity.this,
							android.R.layout.simple_dropdown_item_1line,
							guideSubElements));
					spBooksTypesElements
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									secondDegreeSubElement = spBooksTypesElements
											.getSelectedItem().toString();

									if (secondDegreeSubElement
											.equals("HONOURS")) {
										spThirdDegreeElements
												.setVisibility(View.VISIBLE);
										spThirdDegreeElements
												.setAdapter(new ArrayAdapter<String>(
														AddBookActivity.this,
														android.R.layout.simple_dropdown_item_1line,
														thirdDergeeSubElements));
									} else {
										spThirdDegreeElements
												.setVisibility(View.GONE);
									}

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {

								}
							});
				} else {
					spBooksTypesElements.setAdapter(new ArrayAdapter<String>(
							AddBookActivity.this,
							android.R.layout.simple_dropdown_item_1line,
							textBookSubElements));
					spBooksTypesElements
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									secondDegreeSubElement = spBooksTypesElements
											.getSelectedItem().toString();
									if (secondDegreeSubElement
											.equals("HONOURS")) {
										spThirdDegreeElements
												.setVisibility(View.VISIBLE);
										thirdDergeeSubElements = new String[] {
												"1st Year", "2nd Year",
												"3rd Year" };
										spThirdDegreeElements
												.setAdapter(new ArrayAdapter<String>(
														AddBookActivity.this,
														android.R.layout.simple_dropdown_item_1line,
														thirdDergeeSubElements));
									} else {
										spThirdDegreeElements
												.setVisibility(View.VISIBLE);
										thirdDergeeSubElements = new String[] { "1st Year" };
										spThirdDegreeElements
												.setAdapter(new ArrayAdapter<String>(
														AddBookActivity.this,
														android.R.layout.simple_dropdown_item_1line,
														thirdDergeeSubElements));
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
			}
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

	@Override
	public Object doInBackground() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		// TODO Auto-generated method stub
		
	}
	
	private void getImageFromCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, 100);
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent data) {
		super.onActivityResult(requestCode, responseCode, data);
		if (requestCode == 100) {
			if (responseCode == RESULT_OK) {
				Uri currImageURI = data.getData();
				file = new File(getRealPathFromURI(currImageURI));
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
