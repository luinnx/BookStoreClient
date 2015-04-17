package com.bookstore.app.utils;

import com.androidquery.callback.ImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class CommonValues {
	
	static CommonValues commonValuesInstance;
	
	public boolean IsServerConnectionError = false;	
	public String selectedGroupName;
	public Long notificationMessageTime;
	public String LastCollaborationTime;
	public Boolean isCallFromTTUpdateSet;
	public Boolean isCallFromTTStatusSet;
	public Long LastAlarmTime;
	public Long CollaborationMessageTime;
	public int ExceptionCode = CommonConstraints.NO_EXCEPTION;
	
	
	public int CompanyId=1;
	
	//public User LoginUser;
	
	public Boolean isCallFromNotification=false;
	
	public String LoginUserName=null,LoginUserSecretKey=null;
	public String DeviceSecretKey=null;
	public int userType;
	
	
	public ImageOptions defaultImageOptions;
	public DisplayImageOptions imageOptions =null;
	
	public static CommonValues getInstance(){
		return commonValuesInstance;
	}
	
	public static void initialization(){
		if(commonValuesInstance==null)
			commonValuesInstance=new CommonValues();
	}

}
