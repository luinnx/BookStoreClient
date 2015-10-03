package com.bookstore.app.base;

import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootStartUpReciever extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent arg1) {
		Toast.makeText(context, "Book Store: Boot Complete", Toast.LENGTH_LONG).show();
		if(CommonTasks.getPreferences(context,
				CommonConstraints.USER_ID).equals("")){
			Log.d("BOOT", "Boot Completed : User is not logged in");
			return;
		}else{
		}
		if(CommonTasks.getPreferences(context, CommonConstraints.USER_TYPE).equals("2")){
			if(!CommonTasks.checkServiceIsRunning(context)){
				context.startService(new Intent(context, BookStoreService.class));
			}else{
				Log.d("BOOT", "Boot Completed: Service is already Running");
			}
		}else{
			Log.d("BOOT", "Boot Completed: User is not Agent");
		}
		
		
		
		
	}
}
