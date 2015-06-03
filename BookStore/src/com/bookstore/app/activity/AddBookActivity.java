package com.bookstore.app.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;

public class AddBookActivity extends BookStoreActionBarBase implements
		OnClickListener, IAsynchronousTask {

	Spinner spCategory, spSubCatagory, spTextBooksElements, spSubSubCatagory,
			spPublisher,spBookCondition;
	String[] bookTypes, guideSubElements, textBookSubElements,
			thirdDergeeSubElements;
	String bookType, secondDegreeSubElement;

	ImageView ivCaptureImage, ivPublishDate;
	EditText etBookName, etWritterName, etPublisherName, etBookCondition,
			etBookQuantity, etISBNNumber, etPublishDate, etBookPrice;
	Button btnOk;
	String bookName, writterName, publisherName, bookCondition, bookQuantity,
			isbnNumber, publishDate, bookPrice, imageFilePath = "";
	int category, subCatagory, subSubCatagory;

	DownloadableAsyncTask downloadAsyncTask;
	ProgressDialog dialog;
	File file;
	public byte[] selectedFile;
	public String filename = "";

	private int year;
	private int month;
	private int day;

	AlertDialog alertDialog;
	AlertDialog.Builder myAlertDialog;

	public String[] publications = new String[] { "Srijan Prokash",
			"Grantha kutir", "Golden Future Publications", "Gan Prokash",
			"Dikdarshan Prokashoni Ltd." };
	private String[] bookConditions=new String[]{"New","Used"};

	Uri uriSavedImage;
	int count = 0;
	ArrayList<String> rowPic = new ArrayList<String>();

	protected static final int CAMERA_REQUEST = 0;
	protected static final int GALLERY_PICTURE = 1;
	private Intent pictureActionIntent = null;
	Bitmap bitmap;

	String selectedImagePath;
	
	DatePickerDialog datePickerDialog;

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
		//etBookCondition = (EditText) findViewById(R.id.etBookCondition);
		etBookQuantity = (EditText) findViewById(R.id.etBookQuantity);
		etISBNNumber = (EditText) findViewById(R.id.etISBNNumber);
		etPublishDate = (EditText) findViewById(R.id.etPublishDate);
		etBookPrice = (EditText) findViewById(R.id.etBookPrice);
		ivCaptureImage = (ImageView) findViewById(R.id.ivCaptureImage);
		ivPublishDate = (ImageView) findViewById(R.id.ivPublishDate);

		ivPublishDate.setOnClickListener(this);
		ivCaptureImage.setOnClickListener(this);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);

		spPublisher = (Spinner) findViewById(R.id.spPublisher);
		spPublisher.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, publications));
		
		spBookCondition=(Spinner) findViewById(R.id.spBookCondition);
		
		spBookCondition.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,bookConditions));

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
										subSubCatagory = 11;
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

		spPublisher.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int pos, long arg3) {
				publisherName = parent.getItemAtPosition(pos).toString();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				publisherName = parent.getItemAtPosition(0).toString();

			}
		});
		
		spBookCondition.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int pos, long arg3) {
				bookCondition=parent.getItemAtPosition(pos).toString();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				bookCondition=parent.getItemAtPosition(0).toString();
				
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnOk) {

			if (etBookName.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Enter book name");
				return;
			} else if (etWritterName.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Writter name");
				return;
			}  else if (etBookQuantity.getText().toString().trim().equals("")) {
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
			// publisherName = etPublisherName.getText().toString().trim();
			//bookCondition = etBookCondition.getText().toString().trim();
			bookQuantity = etBookQuantity.getText().toString().trim();
			isbnNumber = etISBNNumber.getText().toString().trim();
			publishDate = etPublishDate.getText().toString().trim();
			bookPrice = etBookPrice.getText().toString().trim();

			if (!CommonTasks.isOnline(this)) {
				CommonTasks.goSettingPage(this);
				return;
			}
			loadInformation();

		} else if (view.getId() == R.id.ivCaptureImage) {
			// getImageFromCamera();
			// getImageOptionDialog();
			startDialog();
		} else if (view.getId() == R.id.ivPublishDate) {
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			showDialog(1);
			// return new DatePickerDialog(this, mDateSetListener, cyear,
			// cmonth, cday);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			datePickerDialog=new DatePickerDialog(this, datePickerListener, year, month,
					day);
			datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
			return datePickerDialog;
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			etPublishDate.setText(new StringBuilder().append(year).append("-")
					.append(month + 1).append("-").append(day).append(" "));

		}
	};

	private void loadInformation() {
		if (downloadAsyncTask != null)
			downloadAsyncTask.cancel(true);
		downloadAsyncTask = new DownloadableAsyncTask(this);
		downloadAsyncTask.execute();

	}

	@Override
	public void showProgressBar() {
		dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		dialog.setMessage("Adding Book , Plaese wait...");
		dialog.setCancelable(false);
		dialog.show();

	}

	@Override
	public void hideProgressBar() {
		dialog.dismiss();

	}

	@Override
	public Object doInBackground() {
		IAdminManager adminManager = new AdminManager();
		return adminManager.addBook(bookName, writterName, publisherName,
				bookCondition, bookPrice, isbnNumber, publishDate,
				selectedFile, bookQuantity, "" + category, "" + subCatagory, ""
						+ subSubCatagory);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			Boolean result = (Boolean) data;
			if (result) {
				CommonTasks.showToast(getApplicationContext(),
						"Add Book Succesfull");
				super.onBackPressed();
			} else {
				CommonTasks.showToast(getApplicationContext(),
						"Book Addition failed, Try again later");
			}
		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error , Please Try again");
		}

	}

	private void startDialog() {

		myAlertDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT);
		//AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View josSubmitView = inflater.inflate(R.layout.image_choser_dialog,
				null);
		myAlertDialog.setView(josSubmitView);

		myAlertDialog.setTitle("Chose Image");
		myAlertDialog.setCancelable(true);
		ImageView ivCameraChooser = (ImageView) josSubmitView
				.findViewById(R.id.ivCameraChooser);
		ImageView ivGalleryChooser = (ImageView) josSubmitView
				.findViewById(R.id.ivGalleryChooser);

		ivGalleryChooser.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pictureActionIntent = new Intent(Intent.ACTION_GET_CONTENT,
						null);
				pictureActionIntent.setType("image/*");
				pictureActionIntent.putExtra("return-data", true);
				startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
				alertDialog.cancel();

			}
		});

		ivCameraChooser.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pictureActionIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(pictureActionIntent, CAMERA_REQUEST);
				alertDialog.cancel();
			}
		});

		alertDialog = myAlertDialog.create();
		alertDialog.show();
	}

	public void getImageOptionDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT);
		LayoutInflater inflater = getLayoutInflater();
		View josSubmitView = inflater.inflate(R.layout.image_choser_dialog,
				null);
		builder.setView(josSubmitView);
		builder.setTitle("Job Submit");
		builder.setCancelable(false);
		ImageView ivCameraChooser = (ImageView) josSubmitView
				.findViewById(R.id.ivCameraChooser);
		ImageView ivGalleryChooser = (ImageView) josSubmitView
				.findViewById(R.id.ivGalleryChooser);

		ivGalleryChooser.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				FromGallery();

			}
		});

		ivCameraChooser.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TakePhoto();

			}
		});

		alertDialog = builder.create();
		alertDialog.show();
	}

	private void TakePhoto() {
		File image = new File(appFolderCheckandCreate(), "img" + getTimeStamp()
				+ ".jpg");
		uriSavedImage = Uri.fromFile(image);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 100);
	}

	private void FromGallery() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("return-data", true);
		imageFilePath = "";
		startActivityForResult(intent, 200);
	}

	

	private String appFolderCheckandCreate() {

		String appFolderPath = "";
		File externalStorage = Environment.getExternalStorageDirectory();

		if (externalStorage.canWrite()) {
			appFolderPath = externalStorage.getAbsolutePath() + "/BookStore";
			File dir = new File(appFolderPath);

			if (!dir.exists()) {
				dir.mkdirs();
			}

		} else {
			CommonTasks.showToast(getApplicationContext(),
					"  Storage media not found or is full ! ");
		}

		return appFolderPath;
	}

	private String getTimeStamp() {

		final long timestamp = new Date().getTime();

		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);

		final String timeString = new SimpleDateFormat("HH_mm_ss_SSS")
				.format(cal.getTime());

		return timeString;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GALLERY_PICTURE) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
						Uri selectedImage = data.getData();
						String wholeID = DocumentsContract
								.getDocumentId(selectedImage);

						// Split at colon, use second item in the array
						String id = wholeID.split(":")[1];

						String[] column = { MediaStore.Images.Media.DATA };

						// where id is equal to
						String sel = MediaStore.Images.Media._ID + "=?";

						Cursor cursor = getContentResolver().query(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								column, sel, new String[] { id }, null);

						String filePath = "";

						int columnIndex = cursor.getColumnIndex(column[0]);

						if (cursor.moveToFirst()) {
							filePath = cursor.getString(columnIndex);
						}

						bitmap = BitmapFactory.decodeFile(filePath); // load
						// preview
						// image
						bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100,
								false);
						// bmpDrawable = new BitmapDrawable(bitmapPreview);
						ivCaptureImage.setImageBitmap(bitmap);

						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						if (bitmap.getByteCount() > (1024 * 1024)) {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 20,
									stream);
						}
						if (bitmap.getByteCount() > (1024 * 512)) {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 40,
									stream);
						}
						if (bitmap.getByteCount() > (1024 * 256)) {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 60,
									stream);
						} else {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
									stream);
						}
						selectedFile = stream.toByteArray();

						cursor.close();
					} else {
					
					// our BitmapDrawable for the thumbnail
					BitmapDrawable bmpDrawable = null;
					// try to retrieve the image using the data from the intent
					Cursor cursor = getContentResolver().query(data.getData(),
							null, null, null, null);
					if (cursor != null) {

						cursor.moveToFirst();

						int idx = cursor.getColumnIndex(ImageColumns.DATA);
						String fileSrc = cursor.getString(idx);
						bitmap = BitmapFactory.decodeFile(fileSrc); // load
																	// preview
																	// image
						bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100,
								false);
						// bmpDrawable = new BitmapDrawable(bitmapPreview);
						ivCaptureImage.setImageBitmap(bitmap);

						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						if (bitmap.getByteCount() > (1024 * 1024)) {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 20,
									stream);
						}
						if (bitmap.getByteCount() > (1024 * 512)) {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 40,
									stream);
						}
						if (bitmap.getByteCount() > (1024 * 256)) {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 60,
									stream);
						} else {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
									stream);
						}
						selectedFile = stream.toByteArray();

					} else {

						bmpDrawable = new BitmapDrawable(getResources(), data
								.getData().getPath());
						ivCaptureImage.setImageDrawable(bmpDrawable);

						bitmap = bmpDrawable.getBitmap();

						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						if (bitmap.getByteCount() > (1024 * 1024)) {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 20,
									stream);
						}
						if (bitmap.getByteCount() > (1024 * 512)) {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 40,
									stream);
						}
						if (bitmap.getByteCount() > (1024 * 256)) {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 60,
									stream);
						} else {
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
									stream);
						}
						selectedFile = stream.toByteArray();
					}

				} 
				}else {
					Toast.makeText(getApplicationContext(), "Cancelled",
							Toast.LENGTH_SHORT).show();
				}
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "Cancelled",
						Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == CAMERA_REQUEST) {
			if (resultCode == RESULT_OK) {
				if (data.hasExtra("data")) {

					// retrieve the bitmap from the intent
					bitmap = (Bitmap) data.getExtras().get("data");

					Cursor cursor = getContentResolver()
							.query(Media.EXTERNAL_CONTENT_URI,
									new String[] {
											Media.DATA,
											Media.DATE_ADDED,
											MediaStore.Images.ImageColumns.ORIENTATION },
									Media.DATE_ADDED, null, "date_added ASC");
					if (cursor != null && cursor.moveToFirst()) {
						do {
							Uri uri = Uri.parse(cursor.getString(cursor
									.getColumnIndex(Media.DATA)));
							selectedImagePath = uri.toString();
						} while (cursor.moveToNext());
						cursor.close();
					}

					Log.e("path of the image from camera ====> ",
							selectedImagePath);

					bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
					// update the image view with the bitmap
					ivCaptureImage.setImageBitmap(bitmap);

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					if (bitmap.getByteCount() > (1024 * 1024)) {
						bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
					}
					if (bitmap.getByteCount() > (1024 * 512)) {
						bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
					}
					if (bitmap.getByteCount() > (1024 * 256)) {
						bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
					} else {
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					}
					selectedFile = stream.toByteArray();
				} else if (data.getExtras() == null) {

					Toast.makeText(getApplicationContext(),
							"No extras to retrieve!", Toast.LENGTH_SHORT)
							.show();

					BitmapDrawable thumbnail = new BitmapDrawable(
							getResources(), data.getData().getPath());

					// update the image view with the newly created drawable
					ivCaptureImage.setImageDrawable(thumbnail);

					bitmap = thumbnail.getBitmap();

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					if (bitmap.getByteCount() > (1024 * 1024)) {
						bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
					}
					if (bitmap.getByteCount() > (1024 * 512)) {
						bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
					}
					if (bitmap.getByteCount() > (1024 * 256)) {
						bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
					} else {
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					}
					selectedFile = stream.toByteArray();

				}

			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "Cancelled",
						Toast.LENGTH_SHORT).show();
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
