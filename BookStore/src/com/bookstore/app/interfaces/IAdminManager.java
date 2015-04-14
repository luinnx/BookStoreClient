package com.bookstore.app.interfaces;

import com.bookstore.app.entities.AgentListRoot;
import com.bookstore.app.entities.BookListRoot;
import com.bookstore.app.entities.JobListRoot;
import com.bookstore.app.entities.TeacherListRoot;

public interface IAdminManager {
	public JobListRoot getJobList(int jobStatus);
	public AgentListRoot getAgentList();
	public BookListRoot getBookList();
	public TeacherListRoot getTeacherList();
	

}
