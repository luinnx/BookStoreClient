package com.bookstore.app.adapters;

import java.util.ArrayList;
import java.util.List;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.bookstore.app.activity.R;
import com.bookstore.app.entities.AgentEntity;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonValues;
import com.bookstore.app.utils.ImageLoader;
import com.mikhaellopez.circularimageview.CircularImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AllUserListAdapter extends ArrayAdapter<AgentEntity> {

	AgentEntity agentEntity;
	ArrayList<AgentEntity> list;
	Context context;

	ImageOptions imgOptions;
	ImageLoader imageLoader;
	private AQuery aq;

	public AllUserListAdapter(Context _context, int textViewResourceId,
			List<AgentEntity> objects) {
		super(_context, textViewResourceId, objects);
		context = _context;
		list = (ArrayList<AgentEntity>) objects;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public AgentEntity getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View jobView = convertView;
		ViewHolder holder = null;
		agentEntity = list.get(position);
		holder = new ViewHolder();

		try {
			if (convertView == null) {

				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				jobView = inflater.inflate(R.layout.all_user_list_item, null);

				holder.ivAgentImage = (CircularImageView) jobView
						.findViewById(R.id.ivBookImage);
				holder.tvAgentName = (TextView) jobView
						.findViewById(R.id.tvAgentName);
				holder.tvAgentAddress = (TextView) jobView
						.findViewById(R.id.tvAgentAddress);
				holder.tvUserType=(TextView) jobView.findViewById(R.id.tvUserType);
				aq = new AQuery(context);
				imageLoader = new ImageLoader(context);
				imgOptions = CommonValues.getInstance().defaultImageOptions;
				imgOptions.targetWidth = 100;
				imgOptions.ratio = 0;
				imgOptions.round = 8;
				jobView.setTag(holder);
			} else {
				holder = (ViewHolder) jobView.getTag();
			}

			holder.tvAgentName.setText("Name : "+agentEntity.full_name);
			
			if(agentEntity.usertype==1){
				holder.tvUserType.setText("User Type : Admin");
			}else{
				holder.tvUserType.setText("User Type : Agent");
			}
			
			if(agentEntity.location_name!=null){
				holder.tvAgentAddress.setText("Location : "+agentEntity.location_name);
			}else{
				holder.tvAgentAddress.setText("Location : "+agentEntity.address);
			}
			
			holder.tvAgentAddress.setVisibility(View.GONE);

			// aq.id(holder.ivAgentImage).image(context.getResources().getDrawable(R.drawable.ic_person_24));
			if (agentEntity.pic_url.equals("")) {
				aq.id(holder.ivAgentImage).image(
						context.getResources().getDrawable(
								R.drawable.ic_person_24));
			} else {

				/*aq.id(holder.ivAgentImage).image(CommonTasks
						.createCircularShape(CommonTasks.getBitmapFromSdCard(
								context, "/sdcard/BookStore/" + ""
										+ agentEntity._id + ".png")));*/
				
				aq.id(holder.ivAgentImage).image((CommonTasks.getBitmapFromSdCard(
								context, "/sdcard/BookStore/" + ""
										+ agentEntity._id + ".png")));
			}

		} catch (Exception ex) {
			CommonTasks.showLogs(context, ex.getMessage());
		}
		return jobView;
	}

	public class ViewHolder {
		public TextView tvAgentName;
		public TextView tvUserType;
		public TextView tvAgentAddress;
		public CircularImageView ivAgentImage;
	}
}
