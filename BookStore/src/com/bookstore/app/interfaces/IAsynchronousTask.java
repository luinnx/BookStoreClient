package com.bookstore.app.interfaces;

public interface IAsynchronousTask {
	
	public void showProgressBar();
	public void hideProgressBar();
	public Object doInBackground();
	public void processDataAfterDownload(Object data);
	

}
