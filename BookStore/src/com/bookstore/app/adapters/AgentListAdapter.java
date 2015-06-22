package com.bookstore.app.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.bookstore.app.activity.R;
import com.bookstore.app.entities.AgentEntity;
import com.bookstore.app.entities.TeacherEntity;
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

public class AgentListAdapter extends ArrayAdapter<AgentEntity> {

	AgentEntity agentEntity;
	ArrayList<AgentEntity> list;
	ArrayList<AgentEntity> listSearch;
	Context context;

	ImageOptions imgOptions;
	ImageLoader imageLoader;
	private AQuery aq;

	public AgentListAdapter(Context _context, int textViewResourceId,
			List<AgentEntity> objects) {
		super(_context, textViewResourceId, objects);
		context = _context;
		list = (ArrayList<AgentEntity>) objects;
		listSearch = new ArrayList<AgentEntity>();
		listSearch.addAll(list);

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
				jobView = inflater.inflate(R.layout.agent_list_item, null);

				holder.ivAgentImage = (CircularImageView) jobView
						.findViewById(R.id.ivBookImage);
				holder.tvAgentName = (TextView) jobView
						.findViewById(R.id.tvAgentName);
				holder.tvAgentAddress = (TextView) jobView
						.findViewById(R.id.tvAgentAddress);
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

			holder.tvAgentName.setText(agentEntity.full_name);

			if (agentEntity.location_name != null) {
				holder.tvAgentAddress.setText(agentEntity.location_name);
			} else {
				holder.tvAgentAddress.setText(agentEntity.address);
			}

			// aq.id(holder.ivAgentImage).image(context.getResources().getDrawable(R.drawable.ic_person_24));
			if (agentEntity.pic_url.equals("")) {
				aq.id(holder.ivAgentImage).image(
						context.getResources().getDrawable(
								R.drawable.ic_person_24));
			} else {

				holder.ivAgentImage.setImageBitmap(CommonTasks
						.createCircularShape(CommonTasks.getBitmapFromSdCard(
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
		public TextView tvAgentAddress;
		public CircularImageView ivAgentImage;
	}

	public void searchAgent(String query) {
		list.clear();
		if (query.length() == 0)
			list.addAll(listSearch);

		else {
			for (AgentEntity agentEntity : this.listSearch) {
				if (agentEntity.full_name.toLowerCase(Locale.getDefault())
						.contains(query)) {
					this.list.add(agentEntity);
				}
			}
		}
		notifyDataSetChanged();
	}
}
