package com.bookstore.app.base;

import java.io.IOException;
import java.net.MalformedURLException;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.callback.ImageOptions;
import com.bookstore.app.activity.R;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.CommonValues;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

public class BookStoreApplication extends Application{

	
	@Override
	public void onCreate() {
		super.onCreate();
		initialization();
		setUpDefaultImageOptions();
		initImageLoader(getApplicationContext());
		
		CommonValues.getInstance().imageOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
		.cacheOnDisk(true).bitmapConfig(Bitmap.Config.ARGB_8888)
		.delayBeforeLoading(1000).resetViewBeforeLoading(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.considerExifParams(true)

		.displayer(new FadeInBitmapDisplayer(300)).build();
		
	}
	
	private void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);

	}
	
	private void setUpDefaultImageOptions() {
		// set the max number of concurrent network connections, default is 4
		AjaxCallback.setNetworkLimit(8);

		// set the max number of icons (image width <= 50) to be cached in
		// memory, default is 20
		BitmapAjaxCallback.setIconCacheLimit(20);

		// set the max number of images (image width > 50) to be cached in
		// memory, default is 20
		BitmapAjaxCallback.setCacheLimit(40);

		// set the max size of an image to be cached in memory, default is 1600
		// pixels (ie. 400x400)
		// BitmapAjaxCallback.setPixelLimit(AQuery.RATIO_PRESERVE);

		// set the max size of the memory cache, default is 1M pixels (4MB)
		BitmapAjaxCallback.setMaxPixelLimit(2000000);

		// Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
		// R.drawable.user_icon);

		CommonValues.getInstance().defaultImageOptions = new ImageOptions();
		CommonValues.getInstance().defaultImageOptions.memCache = false;
		CommonValues.getInstance().defaultImageOptions.fileCache = false;
		CommonValues.getInstance().defaultImageOptions.targetWidth = 0;
		CommonValues.getInstance().defaultImageOptions.fallback = R.drawable.ic_launcher;
		CommonValues.getInstance().defaultImageOptions.preset = null;
		CommonValues.getInstance().defaultImageOptions.animation = 0;
		CommonValues.getInstance().defaultImageOptions.ratio = AQuery.RATIO_PRESERVE;
		CommonValues.getInstance().defaultImageOptions.round = 0;

	}
	
	private void initialization() {
		CommonUrls.initialization();
		CommonValues.initialization();

	}

}
