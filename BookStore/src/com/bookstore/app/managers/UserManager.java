package com.bookstore.app.managers;

import android.util.Log;

import com.bookstore.app.interfaces.IUser;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.JSONfunctions;

public class UserManager implements IUser{

	@Override
	public boolean addGCMID(int userID, int type, String gcmID) {
		boolean result = false;
		try{
			result = (Boolean) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().addGCMID, userID, type, gcmID), Boolean.class);
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}
		return result;
	}

	@Override
	public boolean changePassword(int userID, String oldPassword,
			String newPassword, int type) {
		boolean result = false;
		try{
			result = (Boolean) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().changePassword, userID, type, oldPassword,newPassword), Boolean.class);
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}
		return result;
	}

}
