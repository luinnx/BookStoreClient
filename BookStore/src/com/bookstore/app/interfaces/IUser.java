package com.bookstore.app.interfaces;

public interface IUser {
	public boolean addGCMID(int userID, int type, String gcmID);
	public boolean changePassword(int userID, String oldPassword, String newPassword, int type);
}
