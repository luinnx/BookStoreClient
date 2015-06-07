package com.bookstore.app.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.AgentActionbarBase;
import com.bookstore.app.entities.JobDetails;
import com.bookstore.app.interfaces.IAgent;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AgentManager;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.CommonValues;

public class AgentIndividualJobDetailsActivity extends AgentActionbarBase
		implements OnClickListener, IAsynchronousTask {
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	TextView tvNameBook, tvJobStatus, tvAuthor, tvPublisherName, tvISBN,
			tvQuantity, tvPublishDate, tvPrice, tvTeacherName, tvInstitution,
			tvTeacherMobileNumber, tvAgentName, tvAgentsAddress,
			tvAgentsCurrentLocation, tvAgentsMobileNumber, tvDialogCancel,tvAdminName,
			tvDialogOK;
	LinearLayout tvTakePhoto, tvFromGallery, llImagePanel;
	ImageView ivJobImage, image1, image2, image3, image4;
	EditText etTeacherPassword;
	Button btnOk;
	String jobID = "", mode = "", imageFilePath = "";
	
	AlertDialog alertDialog;
	boolean isJobSubmit = false;
	JobDetails jobDetails = null;
	File file;
	public byte[] selectedFile;
	public String filename = "";
	Uri uriSavedImage;
	JSONObject outerObject;
	JSONArray jsonArr = new JSONArray();
	JSONObject innterObject = new JSONObject();
	int count = 0;
	ArrayList<String> rowPic = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_individual_agent_jod_details);

		NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(3);

		initialization();
	}

	private void initialization() {
		tvNameBook = (TextView) findViewById(R.id.tvNameBook);
		tvJobStatus = (TextView) findViewById(R.id.tvJobStatus);

		tvAuthor = (TextView) findViewById(R.id.tvAuthor);
		tvPublisherName = (TextView) findViewById(R.id.tvPublisherName);
		tvISBN = (TextView) findViewById(R.id.tvISBN);
		tvQuantity = (TextView) findViewById(R.id.tvQuantity);
		tvPublishDate = (TextView) findViewById(R.id.tvPublishDate);
		tvPrice = (TextView) findViewById(R.id.tvPrice);

		tvTeacherName = (TextView) findViewById(R.id.tvTeacherName);
		tvInstitution = (TextView) findViewById(R.id.tvInstitution);
		tvTeacherMobileNumber = (TextView) findViewById(R.id.tvTeacherMobileNumber);

		tvAgentName = (TextView) findViewById(R.id.tvAgentName);
		tvAgentsAddress = (TextView) findViewById(R.id.tvAgentsAddress);
		tvAgentsCurrentLocation = (TextView) findViewById(R.id.tvAgentsCurrentLocation);
		tvAgentsMobileNumber = (TextView) findViewById(R.id.tvAgentsMobileNumber);
		ivJobImage = (ImageView) findViewById(R.id.ivJobImage);
		tvAdminName=(TextView) findViewById(R.id.tvAdminName);

		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.containsKey("JOB_ID")
				&& bundle.containsKey("MODE")) {
			jobID = bundle.getString("JOB_ID");
			mode = bundle.getString("MODE");
		}

		if (mode.equals("2") || mode.equals("3") || mode.equals("4")) {
			btnOk.setText("OK");
		} else {
			btnOk.setText("Submit");
		}

		if (!CommonTasks.isOnline(this)) {
			CommonTasks.goSettingPage(this);
			return;
		}
		LoadInformation();
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnOk) {
			if (btnOk.getText().toString().equals("Submit")) {
				jobSubmit();
			} else {
				onBackPressed();
			}
		}
	}

	private void LoadInformation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressBar() {
		progressDialog = new ProgressDialog(this,
				ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setMessage("Please Wait");
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	public void hideProgressBar() {
		progressDialog.dismiss();
	}

	@Override
	public Object doInBackground() {
		IAgent agent = new AgentManager();
		if (isJobSubmit)
			return agent.jobSubmit(outerObject);
		else {
			return agent.getJobDetails(jobID);
		}

	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if (isJobSubmit) {
				Boolean result = (Boolean) data;
				if (result) {
					count = 0;
					innterObject = new JSONObject();
					jsonArr = new JSONArray();
					outerObject = new JSONObject();
					rowPic = new ArrayList<String>();
					alertDialog.dismiss();
					CommonTasks.showToast(this, "Job Submit done.");
					onBackPressed();
				} else {
					count = 0;
					innterObject = new JSONObject();
					jsonArr = new JSONArray();
					outerObject = new JSONObject();
					rowPic = new ArrayList<String>();
					CommonTasks.showToast(this,
							"UnExpected error. Please try again!");
					alertDialog.dismiss();
				}
			} else {
				jobDetails = (JobDetails) data;
				if (jobDetails != null) {
					setValue(jobDetails);
				}
			}

		}
	}

	private void setValue(JobDetails jobDetails) {
		tvNameBook.setText(jobDetails.BookName);
		if (jobDetails.JobStatus == 3){
			tvJobStatus.setText("Job Status : Complete");
			tvAdminName.setText("Accepted By: "+jobDetails.AdminFullName);
		}
		else if (jobDetails.JobStatus == 1){
			tvJobStatus.setText("Job Status : Pending");
			tvAdminName.setText("Assigned By: "+jobDetails.AdminFullName);
		}
		else if (jobDetails.JobStatus == 2){
			tvJobStatus.setText("Job Status : Submitted");
			tvAdminName.setText("Assigned By: "+jobDetails.AdminFullName);
		}
		else if (jobDetails.JobStatus == 4){
			tvJobStatus.setText("Job Status : Rejected");
			tvAdminName.setText("Rejected By: "+jobDetails.AdminFullName);
		}

		tvAuthor.setText(jobDetails.BookAutherName);
		tvPublisherName.setText(jobDetails.BookPublisherName);
		tvISBN.setText(jobDetails.BookISBNNumber);
		tvQuantity.setText("" + jobDetails.No_Of_Book);
		tvPublishDate.setText(jobDetails.BookPublishDate);
		tvPrice.setText("" + jobDetails.BookPrice);

		tvTeacherName.setText(jobDetails.TeacherName);
		tvInstitution.setText(jobDetails.TeacherInstituteName);
		tvTeacherMobileNumber.setText(jobDetails.TeacherMobileNumber);

		tvAgentName.setText(jobDetails.AgentFullName);
		tvAgentsAddress.setText(jobDetails.AgentAddress);
		tvAgentsCurrentLocation.setText(jobDetails.AgentCurrentLocation);
		tvAgentsMobileNumber.setText(jobDetails.AgentMobileNumber);
	}

	private void jobSubmit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_LIGHT);
		LayoutInflater inflater = getLayoutInflater();
		View josSubmitView = inflater.inflate(R.layout.job_submit_dialog, null);
		builder.setView(josSubmitView);
		builder.setTitle("Job Submit");
		builder.setCancelable(false);

		tvDialogCancel = (TextView) josSubmitView
				.findViewById(R.id.tvDialogCancel);
		tvDialogOK = (TextView) josSubmitView.findViewById(R.id.tvDialogOK);
		tvTakePhoto = (LinearLayout) josSubmitView
				.findViewById(R.id.tvTakePhoto);
		tvFromGallery = (LinearLayout) josSubmitView
				.findViewById(R.id.tvFromGallery);
		llImagePanel = (LinearLayout) josSubmitView
				.findViewById(R.id.llImagePanel);
		image1 = (ImageView) josSubmitView
				.findViewById(R.id.image1);
		image2 = (ImageView) josSubmitView
				.findViewById(R.id.image2);
		image3 = (ImageView) josSubmitView
				.findViewById(R.id.image3);
		image4 = (ImageView) josSubmitView
				.findViewById(R.id.image4);

		tvTakePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TakePhoto();

			}

		});

		tvFromGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FromGallery();

			}

		});

		tvDialogCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});

		tvDialogOK.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				isJobSubmit = true;
				if (!CommonTasks.isOnline(getApplicationContext())) {
					CommonTasks.goSettingPage(getApplicationContext());
					return;
				}
				if (count <= 0) {
					CommonTasks.showToast(getApplicationContext(),
							"No picture selected yet!");
					return;
				}
				int sl = CommonTasks.getPicSlNo();
				for (String string : rowPic) {
					innterObject.put("pic_sl_no", sl);
					innterObject.put("no_of_pic", count);
					innterObject.put("job_status", 2);
					innterObject.put("jobID", Integer.parseInt(jobID));
					innterObject.put("rowImgaeByte", string);
					jsonArr.add(innterObject);
				}
				outerObject = new JSONObject();
				outerObject.put("Job_Submit", jsonArr);
				if (!CommonTasks.isOnline(AgentIndividualJobDetailsActivity.this)) {
					CommonTasks.goSettingPage(AgentIndividualJobDetailsActivity.this);
					return;
				}
				LoadInformation();

			}
		});

		alertDialog = builder.create();
		alertDialog.show();
	}

	private void TakePhoto() {
		File image = new File(appFolderCheckandCreate(), "img" + getTimeStamp()
				+ ".jpg");
		uriSavedImage = Uri.fromFile(image);

		/*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 100);*/
		
		/*pictureActionIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(pictureActionIntent, CAMERA_REQUEST);*/
		
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		//intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
		//intent.putExtra("return-data", true);
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

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent data) {
		super.onActivityResult(requestCode, responseCode, data);
		if (requestCode == 200) {
			if (responseCode == RESULT_OK) {
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

						Bitmap bitmap = BitmapFactory.decodeFile(filePath); // load
						// preview
						// image
						bitmap = Bitmap.createBitmap(bitmap);

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
						count += 1;
						rowPic.add(Base64.encodeToString(selectedFile,
								Base64.NO_WRAP));
						llImagePanel.setVisibility(View.VISIBLE);
						if(count==1){
							image1.setVisibility(View.VISIBLE);
							image1.setImageBitmap(bitmap);
						}else if(count==2){
							image2.setVisibility(View.VISIBLE);
							image2.setImageBitmap(bitmap);
						}else if(count==3){
							image3.setVisibility(View.VISIBLE);
							image3.setImageBitmap(bitmap);
						}else if(count==4){
							image4.setVisibility(View.VISIBLE);
							image4.setImageBitmap(bitmap);
						}else {
							CommonTasks.showToast(this, "Maximum number of image are taken!");
						}

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
							Bitmap bitmap = BitmapFactory.decodeFile(fileSrc); // load
																		// preview
																		// image
							bitmap = Bitmap.createBitmap(bitmap);

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
							count += 1;
							rowPic.add(Base64.encodeToString(selectedFile,
									Base64.NO_WRAP));
							llImagePanel.setVisibility(View.VISIBLE);
							if(count==1){
								image1.setVisibility(View.VISIBLE);
								image1.setImageBitmap(bitmap);
							}else if(count==2){
								image2.setVisibility(View.VISIBLE);
								image2.setImageBitmap(bitmap);
							}else if(count==3){
								image3.setVisibility(View.VISIBLE);
								image3.setImageBitmap(bitmap);
							}else if(count==4){
								image4.setVisibility(View.VISIBLE);
								image4.setImageBitmap(bitmap);
							}else {
								CommonTasks.showToast(this, "Maximum number of image are taken!");
							}

						} else {

							bmpDrawable = new BitmapDrawable(getResources(),
									data.getData().getPath());

							Bitmap bitmap = bmpDrawable.getBitmap();

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
							count += 1;
							rowPic.add(Base64.encodeToString(selectedFile,
									Base64.NO_WRAP));
							llImagePanel.setVisibility(View.VISIBLE);
							if(count==1){
								image1.setVisibility(View.VISIBLE);
								image1.setImageBitmap(bitmap);
							}else if(count==2){
								image2.setVisibility(View.VISIBLE);
								image2.setImageBitmap(bitmap);
							}else if(count==3){
								image3.setVisibility(View.VISIBLE);
								image3.setImageBitmap(bitmap);
							}else if(count==4){
								image4.setVisibility(View.VISIBLE);
								image4.setImageBitmap(bitmap);
							}else {
								CommonTasks.showToast(this, "Maximum number of image are taken!");
							}
						}
					}

				} else {
					Toast.makeText(getApplicationContext(), "Cancelled",
							Toast.LENGTH_SHORT).show();
					
				}
			} else if (responseCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "Cancelled",
						Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == 100) {
			if (responseCode == RESULT_OK) {
				if (data.hasExtra("data")) {

					// retrieve the bitmap from the intent
					Bitmap bitmap = (Bitmap) data.getExtras().get("data");

					bitmap = Bitmap.createBitmap(bitmap);
					// update the image view with the bitmap

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
					count += 1;
					rowPic.add(Base64.encodeToString(selectedFile,
							Base64.NO_WRAP));
					llImagePanel.setVisibility(View.VISIBLE);
					if(count==1){
						image1.setVisibility(View.VISIBLE);
						image1.setImageBitmap(bitmap);
					}else if(count==2){
						image2.setVisibility(View.VISIBLE);
						image2.setImageBitmap(bitmap);
					}else if(count==3){
						image3.setVisibility(View.VISIBLE);
						image3.setImageBitmap(bitmap);
					}else if(count==4){
						image4.setVisibility(View.VISIBLE);
						image4.setImageBitmap(bitmap);
					}else {
						CommonTasks.showToast(this, "Maximum number of image are taken!");
					}
				} else if (data.getExtras() == null) {

					Toast.makeText(getApplicationContext(),
							"No extras to retrieve!", Toast.LENGTH_SHORT)
							.show();

					BitmapDrawable thumbnail = new BitmapDrawable(
							getResources(), data.getData().getPath());

					Bitmap bitmap = thumbnail.getBitmap();

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
					count += 1;
					rowPic.add(Base64.encodeToString(selectedFile,
							Base64.NO_WRAP));
					llImagePanel.setVisibility(View.VISIBLE);
					if(count==1){
						image1.setVisibility(View.VISIBLE);
						image1.setImageBitmap(bitmap);
					}else if(count==2){
						image2.setVisibility(View.VISIBLE);
						image2.setImageBitmap(bitmap);
					}else if(count==3){
						image3.setVisibility(View.VISIBLE);
						image3.setImageBitmap(bitmap);
					}else if(count==4){
						image4.setVisibility(View.VISIBLE);
						image4.setImageBitmap(bitmap);
					}else {
						CommonTasks.showToast(this, "Maximum number of image are taken!");
					}
				}

			} else if (responseCode == RESULT_CANCELED) {
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
