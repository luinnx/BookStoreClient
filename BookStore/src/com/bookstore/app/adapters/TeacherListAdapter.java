package com.bookstore.app.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.bookstore.app.activity.R;
import com.bookstore.app.entities.TeacherEntity;
import com.bookstore.app.utils.CommonTasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TeacherListAdapter extends ArrayAdapter<TeacherEntity> {

	TeacherEntity teacherEntity;
	ArrayList<TeacherEntity> list;
	ArrayList<TeacherEntity> listForSearch;
	Context context;


	public TeacherListAdapter(Context _context, int textViewResourceId,
			List<TeacherEntity> objects) {
		super(_context, textViewResourceId, objects);
		context = _context;
		list = (ArrayList<TeacherEntity>) objects;
		listForSearch=new ArrayList<TeacherEntity>();
		listForSearch.addAll(list);

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public TeacherEntity getItem(int position) {
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
		teacherEntity = list.get(position);
		holder = new ViewHolder();

		try {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				jobView = inflater.inflate(R.layout.teacher_list_item, null);

				
				holder.tvTeacherName = (TextView) jobView
						.findViewById(R.id.tvTeacherName);
				holder.tvInstitutionName = (TextView) jobView
						.findViewById(R.id.tvInstitution);
				holder.tvMobileNumber = (TextView) jobView
						.findViewById(R.id.tvTeachersMobileNumber);
				
				jobView.setTag(holder);
			} else {
				holder = (ViewHolder) jobView.getTag();
			}

			holder.tvInstitutionName.setText("Institute :"+teacherEntity.institute);
			holder.tvTeacherName.setText(teacherEntity.full_name);
			if(teacherEntity.mobile_no!=null&&!teacherEntity.mobile_no.equals("")){
				if(teacherEntity.mobile_no.equals("NULL")){
					holder.tvMobileNumber.setText("Mobile No: UnAvailable");
				}else{
					holder.tvMobileNumber.setText("Mobile No: "+teacherEntity.mobile_no);
				}
					
			}else{
				holder.tvMobileNumber.setVisibility(View.GONE);
			}
			

		} catch (Exception ex) {
			CommonTasks.showLogs(context, ex.getMessage());
		}
		return jobView;
	}

	public class ViewHolder {
		public TextView tvTeacherName;
		public TextView tvInstitutionName;
		public TextView tvMobileNumber;
	}
	
	public void searchTeacher(String charText ){
		charText = charText.toLowerCase(Locale.getDefault());
		this.list.clear();
		
		if(charText.length()==0){
			this.list.addAll(this.listForSearch);
		}else{
			for(TeacherEntity teacherEntity:this.listForSearch){
				if(teacherEntity.institute.toLowerCase(Locale.getDefault()).contains(charText)){
					this.list.add(teacherEntity);
				}
			}
		}
		notifyDataSetChanged();
		
	}
}
