package com.bookstore.app.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bookstore.app.adapters.AgentListAdapter;
import com.bookstore.app.adapters.BookListAdapter;
import com.bookstore.app.adapters.TeacherListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.AgentEntity;
import com.bookstore.app.entities.AgentListRoot;
import com.bookstore.app.entities.BookEntity;
import com.bookstore.app.entities.BookListRoot;
import com.bookstore.app.entities.JobCreateEntity;
import com.bookstore.app.entities.TeacherEntity;
import com.bookstore.app.entities.TeacherListRoot;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

public class CreateJobActivity extends BookStoreActionBarBase implements
		OnClickListener, IAsynchronousTask {

	ImageView ivAddBook;
	Dialog allBookDialog, allTeacherDialog, allAgentDialog,
			selectSpeceficBooksDialog;
	ListView lvAllBookList, lvAllTeacherList, lvAllAgentList;
	BookListAdapter bookListAdapter;
	DownloadableAsyncTask asyncTask;
	ProgressDialog dialog;
	public String MODE = "";
	TextView tvBookName, tvAuthorName, tvPublisherName, tvISBNNumber,
			tvAvailableBooks;
	LinearLayout llBookView, llTeacherView, llAgentView;
	ImageView ivAddTeacher, ivAddAgent;
	TextView tvTeacherName, tvTeacherInstitutionName, tvTeacherMobileNumber;
	EditText etOrderAmountBooks;
	TeacherListAdapter teacherListAdapter;

	AgentListAdapter agentListAdapter;
	TextView tvAgentName, tvAgentAddress, tvAgentMPONumber,
			tvAgentMobileNumber;

	int category, subCatagory;
	String subSubCatagory;
	String[] bookTypes, guideSubElements, textBookSubElements,
			thirdDergeeSubElements;
	String bookType, secondDegreeSubElement;
	Spinner spCategory, spSubCatagory, spSubSubCatagory;
	Button btnSubmit;
	BookEntity bookEntity = null;
	AgentEntity agentEntity = null;
	TeacherEntity teacherEntity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_job);
		initViews();
	}

	private void initViews() {
		tvBookName = (TextView) findViewById(R.id.tvBookName);
		tvAuthorName = (TextView) findViewById(R.id.tvAuthorName);
		tvPublisherName = (TextView) findViewById(R.id.tvPublisherName);
		tvISBNNumber = (TextView) findViewById(R.id.tvISBNNumber);
		tvAvailableBooks = (TextView) findViewById(R.id.tvAvailableBooks);
		llBookView = (LinearLayout) findViewById(R.id.llBookView);
		ivAddBook = (ImageView) findViewById(R.id.ivAddBook);

		tvTeacherName = (TextView) findViewById(R.id.tvTeacherName);
		tvTeacherInstitutionName = (TextView) findViewById(R.id.tvTeacherInstitutionName);
		tvTeacherMobileNumber = (TextView) findViewById(R.id.tvTeacherMobileNumber);
		ivAddTeacher = (ImageView) findViewById(R.id.ivAddTeacher);
		llTeacherView = (LinearLayout) findViewById(R.id.llTeacherView);
		etOrderAmountBooks = (EditText) findViewById(R.id.etOrderAmountBooks);

		ivAddAgent = (ImageView) findViewById(R.id.ivAddAgent);
		tvAgentName = (TextView) findViewById(R.id.tvAgentName);
		tvAgentAddress = (TextView) findViewById(R.id.tvAgentAddress);
		tvAgentMPONumber = (TextView) findViewById(R.id.tvAgentMPONumber);
		tvAgentMobileNumber = (TextView) findViewById(R.id.tvAgentMobileNumber);
		llAgentView = (LinearLayout) findViewById(R.id.llAgentView);

		btnSubmit = (Button) findViewById(R.id.btnSubmit);

		ivAddBook.setOnClickListener(this);
		ivAddTeacher.setOnClickListener(this);
		ivAddAgent.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.ivAddBook) {
			MODE = "BOOK_MODE";
			selectSpeceficBook();

		} else if (view.getId() == R.id.ivAddTeacher) {
			MODE = "TEACHER_MODE";
			if (!CommonTasks.isOnline(this)) {
				CommonTasks.goSettingPage(this);
				return;
			}
			loadInformation();
		} else if (view.getId() == R.id.ivAddAgent) {
			MODE = "AGENT_MODE";
			if (!CommonTasks.isOnline(this)) {
				CommonTasks.goSettingPage(this);
				return;
			}
			loadInformation();
		} else if (view.getId() == R.id.btnSubmit) {
			MODE = "JOB_SUBMIT";

			if (bookEntity == null) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Select a Books Books");
				return;
			} else if (teacherEntity == null) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Select a teacher");
				return;
			} else if (agentEntity == null) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Select an Agent");
				return;
			} else if (etOrderAmountBooks.getText().toString().trim()
					.equals("")) {
				CommonTasks.showToast(getApplicationContext(),
						"Please Enter Amount of Books");
				return;
			} else if (agentEntity.gcm_id != null
					&& !agentEntity.gcm_id.isEmpty()
					&& !agentEntity.gcm_id.equals("null")) {
				if (!CommonTasks.isOnline(this)) {
					CommonTasks.goSettingPage(this);
					return;
				}
				loadInformation();
				
			}else{
				
				CommonTasks.showToast(getApplicationContext(),
						"Agent is not Activated Yet.");
				return;
			}
			
		}

	}

	private void selectSpeceficBook() {
		selectSpeceficBooksDialog = new Dialog(this);
		selectSpeceficBooksDialog
				.setContentView(R.layout.select_specefic_books);
		selectSpeceficBooksDialog.setCancelable(true);
		selectSpeceficBooksDialog.setTitle("Select Specefic Book");
		spCategory = (Spinner) selectSpeceficBooksDialog
				.findViewById(R.id.spCategory);
		spSubCatagory = (Spinner) selectSpeceficBooksDialog
				.findViewById(R.id.spSubCatagory);
		spSubSubCatagory = (Spinner) selectSpeceficBooksDialog
				.findViewById(R.id.spSubSubCatagory);
		Button btnOk = (Button) selectSpeceficBooksDialog
				.findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectSpeceficBooksDialog.dismiss();
				if (!CommonTasks.isOnline(getApplicationContext())) {
					CommonTasks.goSettingPage(getApplicationContext());
					return;
				}
				loadInformation();

			}
		});

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
							CreateJobActivity.this,
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
														CreateJobActivity.this,
														android.R.layout.simple_dropdown_item_1line,
														thirdDergeeSubElements));
										spSubSubCatagory
												.setOnItemSelectedListener(new OnItemSelectedListener() {

													@Override
													public void onItemSelected(
															AdapterView<?> arg0,
															View arg1,
															int arg2, long arg3) {
														subSubCatagory = ""
																+ arg2;

													}

													@Override
													public void onNothingSelected(
															AdapterView<?> arg0) {
														// TODO Auto-generated
														// method stub

													}
												});
									} else {
										subSubCatagory = "-1";
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
							CreateJobActivity.this,
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
														CreateJobActivity.this,
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

														subSubCatagory = ""
																+ position;
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
														CreateJobActivity.this,
														android.R.layout.simple_dropdown_item_1line,
														thirdDergeeSubElements));
										subSubCatagory = "" + 0;
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
		selectSpeceficBooksDialog.show();
	}

	private void loadInformation() {
		if (asyncTask != null)
			asyncTask.cancel(true);
		asyncTask = new DownloadableAsyncTask(this);
		asyncTask.execute();

	}

	private void getAllBookDialog(final BookListRoot entities) {
		allBookDialog = new Dialog(this);
		allBookDialog.setContentView(R.layout.activity_all_book_list);
		allBookDialog.setCancelable(true);
		allBookDialog.setTitle("All Books Dialog");
		lvAllBookList = (ListView) allBookDialog
				.findViewById(R.id.lvAllBookList);
		bookListAdapter = new BookListAdapter(getApplicationContext(),
				R.layout.book_list_item, entities.bookList);
		lvAllBookList.setAdapter(bookListAdapter);
		lvAllBookList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				bookEntity = new BookEntity();
				bookEntity = entities.bookList.get(position);
				tvBookName.setText(bookEntity.full_name);
				tvAuthorName.setText(bookEntity.auther_name);
				tvPublisherName.setText(bookEntity.publisher_name);
				tvISBNNumber.setText(bookEntity.isbn_no);
				tvAvailableBooks.setText("" + bookEntity.avaible);
				llBookView.setVisibility(View.VISIBLE);
				allBookDialog.dismiss();
			}
		});
		allBookDialog.show();

	}

	@Override
	public void showProgressBar() {
		dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		dialog.setMessage("Loading , Plaese wait...");
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
		if (MODE.equals("BOOK_MODE")) {
			return adminManager.querySpeceficTypesBook("" + category, ""
					+ subCatagory, "" + subSubCatagory);
		} else if (MODE.equals("TEACHER_MODE")) {
			return adminManager.getTeacherList();
		} else if (MODE.equals("AGENT_MODE")) {
			return adminManager.getAgentList(0);
		} else if (MODE.equals("JOB_SUBMIT")) {
			/*
			 * String bookName, String bookID, String no_of_book, String
			 * teacherID, String teacher_institute, String jobStatus, String
			 * agentID, String agentGCMID, String adminId)
			 */

			return adminManager
					.createJob(bookEntity.full_name, "" + bookEntity._id, ""
							+ etOrderAmountBooks.getText().toString().trim(),
							"" + teacherEntity._id, teacherEntity.institute, ""
									+ CommonConstraints.PENDING_JOB, ""
									+ agentEntity._id, agentEntity.gcm_id,
							CommonTasks.getPreferences(getApplicationContext(),
									CommonConstraints.USER_ID));
		}
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if (MODE.equals("BOOK_MODE")) {
				BookListRoot bookListRoot = new BookListRoot();
				bookListRoot = (BookListRoot) data;
				if (bookListRoot.bookList.size() > 0) {
					getAllBookDialog(bookListRoot);
				} else {
					CommonTasks.showToast(getApplicationContext(),
							"No Book found");
				}

			} else if (MODE.equals("TEACHER_MODE")) {
				TeacherListRoot teacherListRoot = new TeacherListRoot();
				teacherListRoot = (TeacherListRoot) data;
				getAllTeacherDialog(teacherListRoot);

			} else if (MODE.equals("AGENT_MODE")) {

				AgentListRoot agentListRoot = new AgentListRoot();
				agentListRoot = (AgentListRoot) data;
				getAllAgentDialog(agentListRoot);
			} else if (MODE.equals("JOB_SUBMIT")) {
				JobCreateEntity createEntity = new JobCreateEntity();
				createEntity = (JobCreateEntity) data;
				if (createEntity.status) {
					CommonTasks.showToast(getApplicationContext(),
							createEntity.message);
					super.onBackPressed();
				} else {
					CommonTasks.showToast(getApplicationContext(),
							createEntity.message);
				}
			}
		} else {
			CommonTasks.showToast(getApplicationContext(),
					"Internal Server Error. Please try again later");
		}

	}

	private void getAllAgentDialog(final AgentListRoot entities) {
		// allAgentDialog
		allAgentDialog = new Dialog(this);
		allAgentDialog.setContentView(R.layout.activity_all_agent_list);
		allAgentDialog.setCancelable(true);
		allAgentDialog.setTitle("All Agents ");
		lvAllAgentList = (ListView) allAgentDialog
				.findViewById(R.id.lvAllAgentList);
		agentListAdapter = new AgentListAdapter(getApplicationContext(),
				R.layout.agent_list_item, entities.agentList);
		lvAllAgentList.setAdapter(agentListAdapter);
		lvAllAgentList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				agentEntity = new AgentEntity();
				agentEntity = entities.agentList.get(position);

				tvAgentName.setText(agentEntity.full_name);
				tvAgentAddress.setText(agentEntity.address);
				tvAgentMobileNumber.setText(agentEntity.mobile_no);
				tvAgentMPONumber.setText(agentEntity.mpo_no);

				llAgentView.setVisibility(View.VISIBLE);
				allAgentDialog.dismiss();
			}
		});
		allAgentDialog.show();

	}

	private void getAllTeacherDialog(final TeacherListRoot entities) {

		allTeacherDialog = new Dialog(this);
		allTeacherDialog.setContentView(R.layout.activity_all_teacher_list);
		allTeacherDialog.setCancelable(true);
		allTeacherDialog.setTitle("All Teachers ");
		lvAllTeacherList = (ListView) allTeacherDialog
				.findViewById(R.id.lvAllTeacherList);
		teacherListAdapter = new TeacherListAdapter(getApplicationContext(),
				R.layout.teacher_list_item, entities.teacherList);
		lvAllTeacherList.setAdapter(teacherListAdapter);
		lvAllTeacherList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				teacherEntity = new TeacherEntity();
				teacherEntity = entities.teacherList.get(position);

				tvTeacherName.setText(teacherEntity.full_name);
				tvTeacherInstitutionName.setText(teacherEntity.institute);
				tvTeacherMobileNumber.setText(teacherEntity.mobile_no);

				llTeacherView.setVisibility(View.VISIBLE);
				allTeacherDialog.dismiss();
			}
		});
		allTeacherDialog.show();

	}

}
