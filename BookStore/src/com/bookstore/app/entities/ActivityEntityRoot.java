package com.bookstore.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ActivityEntityRoot {
	
	@SerializedName("DailyActivity")
	public List<ActivityEntity> activityList=null;
	
	public ActivityEntityRoot() {
		activityList=new ArrayList<ActivityEntity>();
	}

}
