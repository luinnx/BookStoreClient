package com.bookstore.app.managers;

import org.json.simple.JSONObject;

import android.util.Log;

import com.bookstore.app.interfaces.IUser;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.JSONfunctions;

public class UserManager implements IUser{

	@SuppressWarnings("unchecked")
	@Override
	public boolean addGCMID(int userID, String gcmID) {
		boolean result = false;
		try{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userID", userID);
			jsonObject.put("gcmID", gcmID);
			result = (Boolean) JSONfunctions.retrieveDataFromJsonPostURL(CommonUrls.getInstance().addGCMID, jsonObject, Boolean.class);
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
			result = (Boolean) JSONfunctions.retrieveDataFromStream(String.format(CommonUrls.getInstance().changePassword, userID, oldPassword,newPassword), Boolean.class);
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}
		return result;
	}

}
