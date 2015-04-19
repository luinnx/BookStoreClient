package com.bookstore.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class TeacherListRoot {
	@SerializedName("TeacherList")
	public List<TeacherEntity> teacherList;
	
	public TeacherListRoot(){
		teacherList=new ArrayList<TeacherEntity>();
	}

}
