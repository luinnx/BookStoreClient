package com.bookstore.app.adapters;

import java.util.ArrayList;
import java.util.List;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.bookstore.app.activity.R;
import com.bookstore.app.entities.JobEntity;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.CommonValues;
import com.bookstore.app.utils.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class JobListAdapter extends ArrayAdapter<JobEntity> {

	JobEntity jobEntity;
	ArrayList<JobEntity> list;
	Context context;
	
	ImageOptions imgOptions;
	ImageLoader imageLoader;
	private AQuery aq;

	public JobListAdapter(Context _context, int textViewResourceId,
			List<JobEntity> objects) {
		super(_context, textViewResourceId, objects);
		context=_context;
		list = (ArrayList<JobEntity>) objects;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public JobEntity getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View jobView=convertView;
		ViewHolder holder = null;
		jobEntity=list.get(position);
		holder=new ViewHolder();
		
		try{
			if( convertView==null){
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				jobView = inflater.inflate(R.layout.job_list_item, null);
				
				holder.ivBookImage = (ImageView) jobView.findViewById(R.id.ivBookImage);
				holder.pbImagePreLoad = (ProgressBar) jobView.findViewById(R.id.pbImagePreLoad);
				holder.tvAgentName = (TextView) jobView.findViewById(R.id.tvAgentName);
				holder.tvBookName = (TextView) jobView.findViewById(R.id.tvBookName);
				holder.tvNumberOfBook = (TextView) jobView.findViewById(R.id.tvNumberOfBook);
				aq = new AQuery(context);
				imageLoader = new ImageLoader(context);
				imgOptions = CommonValues.getInstance().defaultImageOptions; 		
				imgOptions.targetWidth=100;
				imgOptions.ratio=0;
				imgOptions.round = 8;
				jobView.setTag(holder);
			}else{
				holder = (ViewHolder) jobView.getTag();
			}
			
			holder.tvAgentName.setText(jobEntity.agentname);
			holder.tvBookName.setText(jobEntity.bookname);
			holder.tvNumberOfBook.append(""+jobEntity.quantity);
			
			if(jobEntity.bookImage != null){
				aq.id(holder.ivBookImage).image(context.getResources().getDrawable(R.drawable.ic_launcher));
			}else{
				aq.id(holder.ivBookImage).image((CommonUrls.getInstance().IMAGE_BASE_URL+jobEntity.bookImage.toString()),imgOptions);
			}
			
		}catch(Exception ex){
			CommonTasks.showLogs(context, ex.getMessage());
		}
		return jobView;
	}

	public class ViewHolder {
		public TextView tvAgentName;
		public TextView tvBookName;
		public TextView tvNumberOfBook;
		public ImageView ivBookImage;
		public ProgressBar pbImagePreLoad;
	}
}
