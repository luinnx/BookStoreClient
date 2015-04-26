package com.bookstore.app.adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.bookstore.app.activity.R;
import com.bookstore.app.entities.DonationEntity;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.CommonValues;
import com.bookstore.app.utils.ImageLoader;

public class DonationListAdapter extends ArrayAdapter<DonationEntity> {

	DonationEntity donation;
	ArrayList<DonationEntity> list;
	Context context;
	
	ImageOptions imgOptions;
	ImageLoader imageLoader;
	private AQuery aq;

	public DonationListAdapter(Context _context, int textViewResourceId,
			List<DonationEntity> objects) {
		super(_context, textViewResourceId, objects);
		context=_context;
		list = (ArrayList<DonationEntity>) objects;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public DonationEntity getItem(int position) {
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
		donation=list.get(position);
		holder=new ViewHolder();
		
		try{
			if( convertView==null){
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				jobView = inflater.inflate(R.layout.donation_list_items, null);
				
				holder.ivAgentImage = (ImageView) jobView.findViewById(R.id.ivAgentImage);
				holder.pbImagePreLoad = (ProgressBar) jobView.findViewById(R.id.pbImagePreLoad);
				holder.tvAgentName = (TextView) jobView.findViewById(R.id.tvAgentName);
				holder.tvDonationAmount = (TextView) jobView.findViewById(R.id.tvDonationAmount);
				holder.tvDonationDate = (TextView) jobView.findViewById(R.id.tvDonationDate);
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
			
			holder.tvAgentName.setText(donation.agent_name);
			holder.tvDonationAmount.setText("Amount :"+String.valueOf(donation.Amount));
			holder.tvDonationDate.setText("Date :"+ (String) DateUtils.getRelativeTimeSpanString(
					donation.date, new Date().getTime(), DateUtils.DAY_IN_MILLIS));
			
			if(donation.pic_url != null){
				aq.id(holder.ivAgentImage).image(context.getResources().getDrawable(R.drawable.ic_person));
			}else{
				aq.id(holder.ivAgentImage).image((CommonUrls.getInstance().IMAGE_BASE_URL+donation.pic_url.toString()),imgOptions);
			}
			
		}catch(Exception ex){
			CommonTasks.showLogs(context, ex.getMessage());
		}
		return jobView;
	}
	
	
	public class ViewHolder {
		public TextView tvAgentName;
		public TextView tvDonationAmount;
		public TextView tvDonationDate;
		public ImageView ivAgentImage;
		public ProgressBar pbImagePreLoad;
	}

}
