package com.bookstore.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class JobListRoot {
	@SerializedName("JobList")
	public List<JobEntity> jobList;
	
	public JobListRoot(){
		jobList=new ArrayList<JobEntity>();
	}

}
