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

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonTasks;

public class AddAgentActivity extends BookStoreActionBarBase implements
		OnClickListener, IAsynchronousTask {

	ImageView ivCaptureImage;
	EditText etAgentname, etAgentEmail, etAgentAddress, etAgentMPONumber,
			etAgentMobileNumber, etAgentPassword;
	Button btnOk;
	DownloadableAsyncTask downloadAsyncTask;
	ProgressDialog dialog;

	File file;
	public byte[] selectedFile;
	public String filename = "";
	Uri uriSavedImage;

	protected static final int CAMERA_REQUEST = 0;
	protected static final int GALLERY_PICTURE = 1;
	private Intent pictureActionIntent = null;
	Bitmap bitmap;
	AlertDialog alertDialog;
	AlertDialog.Builder myAlertDialog;

	String selectedImagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_agent);
		initalization();
	}

	private void initalization() {
		etAgentname = (EditText) findViewById(R.id.etAgentname);
		etAgentEmail = (EditText) findViewById(R.id.etAgentEmail);
		etAgentAddress = (EditText) findViewById(R.id.etAgentAddress);
		etAgentMPONumber = (EditText) findViewById(R.id.etAgentMPONumber);
		etAgentMobileNumber = (EditText) findViewById(R.id.etAgentMobileNumber);
		etAgentPassword = (EditText) findViewById(R.id.etAgentPassword);
		ivCaptureImage = (ImageView) findViewById(R.id.ivCaptureImage);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);
		ivCaptureImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.ivCaptureImage) {
			// getImageFromCamera();
			startDialog();
		} else {
			if (etAgentname.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Enter Agent NAme");
				return;
			} else if (etAgentEmail.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Enter Agent Email");
				return;
			} else if (etAgentAddress.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Enter Agent Address");
				return;
			} else if (etAgentMPONumber.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Enter Agent MPO Number");
				return;
			} else if (etAgentMobileNumber.getText().toString().trim()
					.equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Enter Agent Mobile Number");
				return;
			} else if (etAgentPassword.getText().toString().trim().equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Enter Agent Mobile Password");
				return;
			}
			if (!CommonTasks.isOnline(this)) {
				CommonTasks.goSettingPage(this);
				return;
			}
			LoginRequest();
		}

	}

	private void LoginRequest() {
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
		IAdminManager adminManager = new AdminManager();
		return adminManager.createAgent(etAgentname.getText().toString(),
				etAgentPassword.getText().toString().trim(), etAgentEmail
						.getText().toString(), etAgentMobileNumber.getText()
						.toString(), etAgentAddress.getText().toString(),
				selectedFile, etAgentMPONumber.getText().toString(), 1, 2);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			Boolean boolean1 = (Boolean) data;
			if (boolean1) {
				CommonTasks.showToast(getApplicationContext(),
						"Agent Creation Succesfull.");
				super.onBackPressed();
			} else {
				CommonTasks.showToast(getApplicationContext(),
						"Agent Creation failed. Please try again later.");
			}
		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please try again later.");
		}

	}

	private void startDialog() {

		myAlertDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT);
		// AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
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
						// try to retrieve the image using the data from the
						// intent
						Cursor cursor = getContentResolver().query(
								data.getData(), null, null, null, null);
						if (cursor != null) {

							cursor.moveToFirst();

							int idx = cursor.getColumnIndex(ImageColumns.DATA);
							String fileSrc = cursor.getString(idx);
							bitmap = BitmapFactory.decodeFile(fileSrc); // load
																		// preview
																		// image
							bitmap = Bitmap.createScaledBitmap(bitmap, 100,
									100, false);
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
								bitmap.compress(Bitmap.CompressFormat.JPEG,
										100, stream);
							}
							selectedFile = stream.toByteArray();

						} else {

							bmpDrawable = new BitmapDrawable(getResources(),
									data.getData().getPath());
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
								bitmap.compress(Bitmap.CompressFormat.JPEG,
										100, stream);
							}
							selectedFile = stream.toByteArray();
						}
					}

				} else {
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

	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
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

	// this is for test

}
