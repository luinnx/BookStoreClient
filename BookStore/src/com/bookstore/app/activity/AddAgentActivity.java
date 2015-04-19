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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
			getImageFromCamera();
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
		}

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
