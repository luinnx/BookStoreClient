package com.bookstore.app.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bookstore.app.adapters.AgentListAdapter;
import com.bookstore.app.adapters.BookListAdapter;
import com.bookstore.app.adapters.TeacherListAdapter;
import com.bookstore.app.asynctasks.DownloadableAsyncTask;
import com.bookstore.app.base.BookStoreActionBarBase;
import com.bookstore.app.entities.AgentEntity;
import com.bookstore.app.entities.BookEntity;
import com.bookstore.app.entities.TeacherEntity;
import com.bookstore.app.interfaces.IAdminManager;
import com.bookstore.app.interfaces.IAsynchronousTask;
import com.bookstore.app.managers.AdminManager;

public class CreateJobActivity extends BookStoreActionBarBase implements
		OnClickListener, IAsynchronousTask {

	ImageView ivAddBook;
	Dialog allBookDialog,allTeacherDialog,allAgentDialog;
	ListView lvAllBookList,lvAllTeacherList,lvAllAgentList;
	BookListAdapter bookListAdapter;
	DownloadableAsyncTask asyncTask;
	ProgressDialog dialog;
	public String MODE="";
	TextView tvBookName,tvAuthorName,tvPublisherName,tvISBNNumber,tvAvailableBooks;
	LinearLayout llBookView,llTeacherView,llAgentView;
	ImageView ivAddTeacher,ivAddAgent;
	TextView tvTeacherName,tvTeacherInstitutionName,tvTeacherMobileNumber;
	TeacherListAdapter teacherListAdapter;
	
	AgentListAdapter agentListAdapter;
	TextView tvAgentName,tvAgentAddress,tvAgentMPONumber,tvAgentMobileNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_job);
		initViews();
	}

	private void initViews() {
		tvBookName=(TextView) findViewById(R.id.tvBookName);
		tvAuthorName=(TextView) findViewById(R.id.tvAuthorName);
		tvPublisherName=(TextView) findViewById(R.id.tvPublisherName);
		tvISBNNumber=(TextView) findViewById(R.id.tvISBNNumber);
		tvAvailableBooks=(TextView) findViewById(R.id.tvAvailableBooks);
		llBookView=(LinearLayout) findViewById(R.id.llBookView);
		ivAddBook = (ImageView) findViewById(R.id.ivAddBook);
		
		
		tvTeacherName=(TextView) findViewById(R.id.tvTeacherName);
		tvTeacherInstitutionName=(TextView) findViewById(R.id.tvTeacherInstitutionName);
		tvTeacherMobileNumber=(TextView) findViewById(R.id.tvTeacherMobileNumber);
		ivAddTeacher=(ImageView) findViewById(R.id.ivAddTeacher);
		llTeacherView=(LinearLayout) findViewById(R.id.llTeacherView);
		
		ivAddAgent=(ImageView) findViewById(R.id.ivAddAgent);
		tvAgentName=(TextView) findViewById(R.id.tvAgentName);
		tvAgentAddress=(TextView) findViewById(R.id.tvAgentAddress);
		tvAgentMPONumber=(TextView) findViewById(R.id.tvAgentMPONumber);
		tvAgentMobileNumber=(TextView) findViewById(R.id.tvAgentMobileNumber);
		llAgentView=(LinearLayout) findViewById(R.id.llAgentView);
		
		ivAddBook.setOnClickListener(this);
		ivAddTeacher.setOnClickListener(this);
		ivAddAgent.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.ivAddBook) {
			MODE="BOOK_MODE";
			loadInformation();
		}else if (view.getId() == R.id.ivAddTeacher) {
			MODE="TEACHER_MODE";
			loadInformation();
		}else if (view.getId() == R.id.ivAddAgent) {
			MODE="AGENT_MODE";
			loadInformation();
		}

	}

	private void loadInformation() {
		if(asyncTask!=null)
			asyncTask.cancel(true);
		asyncTask=new DownloadableAsyncTask(this);
		asyncTask.execute();
		
	}

	private void getAllBookDialog(final ArrayList<BookEntity> entities) {
		allBookDialog = new Dialog(this);
		allBookDialog.setContentView(R.layout.activity_all_book_list);
		allBookDialog.setCancelable(true);
		allBookDialog.setTitle("All Books Dialog");
		lvAllBookList = (ListView) allBookDialog
				.findViewById(R.id.lvAllBookList);
		bookListAdapter=new BookListAdapter(getApplicationContext(), R.layout.book_list_item, entities);
		lvAllBookList.setAdapter(bookListAdapter);
		lvAllBookList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				BookEntity bookEntity=new BookEntity();
				bookEntity=entities.get(position);
				tvBookName.setText(bookEntity.full_name);
				tvAuthorName.setText(bookEntity.Auther_name);
				tvPublisherName.setText(bookEntity.publisher_name);
				tvISBNNumber.setText(bookEntity.isbn_no);
				tvAvailableBooks.setText(""+bookEntity.avaible);
				llBookView.setVisibility(View.VISIBLE);
				allBookDialog.dismiss();
			}
		});
		allBookDialog.show();

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
		IAdminManager adminManager=new AdminManager();
		if(MODE.equals("BOOK_MODE")){
			return adminManager.getBookList();
		}else if(MODE.equals("TEACHER_MODE")){
			return adminManager.getTeacherList();
		}else if(MODE.equals("AGENT_MODE")){
			return adminManager.getAgentList();
		}
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(MODE.equals("BOOK_MODE")){
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
			getAllBookDialog(entities);
		}else if(MODE.equals("TEACHER_MODE")){
			ArrayList<TeacherEntity> entities = new ArrayList<TeacherEntity>();
			TeacherEntity entity = new TeacherEntity();
			entity._id = 1;
			entity.full_name = "Anisur Rahman";
			entity.institute = "Alimuddin Degree Colliage";
			entity.mobile_no = "01739856984";
			entities.add(entity);

			entity = new TeacherEntity();
			entity._id = 1;
			entity.full_name = "Shariful Islam";
			entity.institute = "Nilphamari Degree Colliage";
			entity.mobile_no = "0173966600";
			entities.add(entity);
			getAllTeacherDialog(entities);
			
		}else if(MODE.equals("AGENT_MODE")){
			ArrayList<AgentEntity> entities = new ArrayList<AgentEntity>();
			AgentEntity entity = new AgentEntity();
			entity._id = 1;
			entity.address = "Nilphamari";
			entity.create_date = "20 jan , 2015";
			entity.email = "mesukcse08@gmail.com";
			entity.full_name = "Md. Sajedul Karim";
			entity.gcm_id = "12354875wdhwhdgjwh";
			entity.isActive = 1;
			entity.mobile_no = "01737186095";
			entity.mpo_no = "kutir123";
			entity.pic_url = "";
			entities.add(entity);

			entity = new AgentEntity();
			entity._id = 2;
			entity.address = "Jamal Pur";
			entity.create_date = "20 Dec , 2014";
			entity.email = "mhasan@gmail.com";
			entity.full_name = "Md. Mahbub hasan";
			entity.gcm_id = "1jjghs4875wdhwhdgjwh";
			entity.isActive = 1;
			entity.mobile_no = "01937569856";
			entity.mpo_no = "kutir124";
			entity.pic_url = "";
			entities.add(entity);
			getAllAgentDialog(entities);
		}
		
	}

	private void getAllAgentDialog(final ArrayList<AgentEntity> entities) {
		//allAgentDialog
		allAgentDialog = new Dialog(this);
		allAgentDialog.setContentView(R.layout.activity_all_agent_list);
		allAgentDialog.setCancelable(true);
		allAgentDialog.setTitle("All Agents ");
		lvAllAgentList = (ListView) allAgentDialog
				.findViewById(R.id.lvAllAgentList);
		agentListAdapter=new AgentListAdapter(getApplicationContext(), R.layout.agent_list_item, entities);
		lvAllAgentList.setAdapter(agentListAdapter);
		lvAllAgentList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				AgentEntity agentEntity=new AgentEntity();
				agentEntity=entities.get(position);
				
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

	private void getAllTeacherDialog(final ArrayList<TeacherEntity> entities) {
				
		allTeacherDialog = new Dialog(this);
		allTeacherDialog.setContentView(R.layout.activity_all_teacher_list);
		allTeacherDialog.setCancelable(true);
		allTeacherDialog.setTitle("All Teachers ");
		lvAllTeacherList = (ListView) allTeacherDialog
				.findViewById(R.id.lvAllTeacherList);
		teacherListAdapter=new TeacherListAdapter(getApplicationContext(), R.layout.teacher_list_item, entities);
		lvAllTeacherList.setAdapter(teacherListAdapter);
		lvAllTeacherList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				TeacherEntity teacherEntity=new TeacherEntity();
				teacherEntity=entities.get(position);
				
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
