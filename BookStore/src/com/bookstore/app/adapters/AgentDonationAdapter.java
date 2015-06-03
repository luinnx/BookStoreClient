package com.bookstore.app.adapters;

import java.util.ArrayList;
import java.util.Date;

import com.bookstore.app.activity.R;
import com.bookstore.app.entities.AgentDonationEntity;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AgentDonationAdapter extends ArrayAdapter<AgentDonationEntity> {
	
	Context context;
	ArrayList<AgentDonationEntity> donationEntityList;
	AgentDonationEntity agentDonationEntity;
	

	public AgentDonationAdapter(Context context, int resource,
			ArrayList<AgentDonationEntity> objects) {
		super(context, resource, objects);
		this.context = context;
		this.donationEntityList = objects;
	}
	
	@Override
	public int getCount() {
		return this.donationEntityList.size();
	}
	
	@Override
	public AgentDonationEntity getItem(int position) {
		return this.donationEntityList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return this.donationEntityList.get(position).hashCode();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View donationView = convertView;
		final ViewHolder holder;
		try{
			agentDonationEntity = donationEntityList.get(position);
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
				donationView = inflater.inflate(R.layout.adapter_donation_item, null);	
				holder = new ViewHolder();
				holder.donationID = (TextView) donationView.findViewById(R.id.donationID);
				holder.donationAmount = (TextView) donationView.findViewById(R.id.donationAmount);
				holder.donationDate = (TextView) donationView.findViewById(R.id.donationDate);
				holder.donationStatus = (TextView) donationView.findViewById(R.id.donationStatus);
				donationView.setTag(holder);
			}else{
				holder = (ViewHolder) donationView.getTag();
			}
			holder.donationID.setText("WR-DONATION-ID: " + agentDonationEntity.id);
			holder.donationAmount.setText("Amount: " + agentDonationEntity.totalAmount);
			holder.donationDate.setText("Date: " + (String) DateUtils.getRelativeTimeSpanString(
					agentDonationEntity.cretedate, new Date().getTime(), DateUtils.DAY_IN_MILLIS));
			if(agentDonationEntity.status == 1){
				holder.donationStatus.setText("Status: Pending");
			}else if(agentDonationEntity.status == 2){
				holder.donationStatus.setText("Status: Completed");
			}else{
				holder.donationStatus.setText("Status: Rejected");
			}
			
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}
		return donationView;
	}
	
	public class ViewHolder{
		TextView donationID, donationAmount, donationDate, donationStatus;
	}

}
