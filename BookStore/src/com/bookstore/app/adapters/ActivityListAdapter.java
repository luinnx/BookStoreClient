package com.bookstore.app.adapters;

import java.util.ArrayList;
import java.util.List;

import com.bookstore.app.activity.R;
import com.bookstore.app.entities.ActivityEntity;
import com.bookstore.app.utils.CommonTasks;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ActivityListAdapter extends ArrayAdapter<ActivityEntity> {

	ActivityEntity jobEntity;
	ArrayList<ActivityEntity> list;
	Context context;

	public ActivityListAdapter(Context _context, int textViewResourceId,
			List<ActivityEntity> objects) {
		super(_context, textViewResourceId, objects);
		context = _context;
		list = (ArrayList<ActivityEntity>) objects;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ActivityEntity getItem(int position) {
		// TODO Auto-generated method stub
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
		jobEntity = list.get(position);
		holder = new ViewHolder();

		try {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				jobView = inflater.inflate(R.layout.activity_list_item, null);

				holder.tvActivityId = (TextView) jobView
						.findViewById(R.id.tvActivityId);
				holder.tvAgentname = (TextView) jobView
						.findViewById(R.id.tvAgentname);
				holder.tvNoOfBook = (TextView) jobView
						.findViewById(R.id.tvNoOfBook);
				holder.tvDate = (TextView) jobView.findViewById(R.id.tvDate);

				jobView.setTag(holder);
			} else {
				holder = (ViewHolder) jobView.getTag();
			}

			holder.tvActivityId.setText(""+jobEntity.id);
			holder.tvAgentname.setText(jobEntity.book_name);
			holder.tvNoOfBook.setText("" + jobEntity.book_quantity);
			holder.tvDate.setText(jobEntity.activity_date);

		} catch (Exception ex) {
			CommonTasks.showLogs(context, ex.getMessage());
		}
		return jobView;
	}

	public class ViewHolder {
		public TextView tvActivityId, tvAgentname, tvNoOfBook, tvDate;;
	}
}
