package com.bookstore.app.asynctasks;


import com.bookstore.app.interfaces.IAsynchronousTask;

import android.os.AsyncTask;
import android.util.Log;

public class DownloadableAsyncTask extends AsyncTask<Void, Void, Object> {

	IAsynchronousTask asynchronousTask;

	public DownloadableAsyncTask(IAsynchronousTask activity) {
		this.asynchronousTask = activity;
	}

	@Override
	protected void onPreExecute() {
		if (asynchronousTask != null)
			asynchronousTask.showProgressBar();
	}

	@Override
	protected Object doInBackground(Void... params) {
		try {
			if (asynchronousTask != null) {
				return asynchronousTask.doInBackground();
			}
		} catch (Exception ex) {
			Log.d("BSS", ex.getMessage()==null?"":ex.getMessage());
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		if (asynchronousTask != null) {
			asynchronousTask.hideProgressBar();
			asynchronousTask.processDataAfterDownload(result);
		}
	}

}
