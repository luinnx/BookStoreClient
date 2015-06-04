package com.bookstore.app.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.bookstore.app.activity.R;
import com.bookstore.app.entities.TadaListEntity;
import com.bookstore.app.utils.CommonConstraints;
import com.bookstore.app.utils.CommonTasks;
import com.bookstore.app.utils.CommonUrls;
import com.bookstore.app.utils.CommonValues;
import com.bookstore.app.utils.ImageLoader;
import com.mikhaellopez.circularimageview.CircularImageView;

public class TaDaListAdapter extends ArrayAdapter<TadaListEntity> {

	TadaListEntity taDaEntity;
	ArrayList<TadaListEntity> list;
	Context context;

	ImageOptions imgOptions;
	ImageLoader imageLoader;
	private AQuery aq;

	public TaDaListAdapter(Context _context, int textViewResourceId,
			List<TadaListEntity> objects) {
		super(_context, textViewResourceId, objects);
		context = _context;
		list = (ArrayList<TadaListEntity>) objects;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public TadaListEntity getItem(int position) {
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
		taDaEntity=list.get(position);
		
		
		try{
			if( convertView==null){
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				jobView = inflater.inflate(R.layout.tada_list_item, null);
				holder=new ViewHolder();
				
				holder.ivAgentImage = (CircularImageView) jobView.findViewById(R.id.ivAgentImage);
				holder.tvAgentName = (TextView) jobView.findViewById(R.id.tvAgentName);
				holder.tvAgentAddress = (TextView) jobView.findViewById(R.id.tvAgentAddress);
				holder.tvTotalAmount = (TextView) jobView.findViewById(R.id.tvTotalAmount);
				holder.tvDistance = (TextView) jobView.findViewById(R.id.tvDistance);
				holder.tvTadaID=(TextView) jobView.findViewById(R.id.tvTadaID);
				
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
			
			holder.tvTadaID.setText("WR-TADA-ID : "+taDaEntity.id);
			holder.tvAgentName.setText("Agent Name : "+taDaEntity.agentname);
			holder.tvDistance.setText("Distance : "+taDaEntity.distance+" KM");
			holder.tvTotalAmount.setText("Total Amount : "+taDaEntity.totalamount +"Tk.");
			
			
			if (taDaEntity.agentpic != null) {
				aq.id(holder.ivAgentImage).image(
						context.getResources().getDrawable(
								R.drawable.ic_person_24));
			} else {

				holder.ivAgentImage.setImageBitmap(CommonTasks
						.createCircularShape(CommonTasks.getBitmapFromSdCard(
								context, "/sdcard/BookStore/" + ""
										+ taDaEntity.agentid + ".png")));
			}
			
			/*if(!taDaEntity.agentpic .equals("")){
				aq.id(holder.ivAgentImage).image((CommonUrls.getInstance().IMAGE_BASE_URL+taDaEntity.agentpic.toString()),imgOptions);
			}else{
				aq.id(holder.ivAgentImage).image(context.getResources().getDrawable(R.drawable.ic_person));
			}*/
			
		}catch(Exception ex){
			CommonTasks.showLogs(context, ex.getMessage());
		}
		return jobView;
	}

	public class ViewHolder {
		public TextView tvAgentName;
		public TextView tvAgentAddress;
		public CircularImageView ivAgentImage;
		public TextView tvTotalAmount;
		public TextView tvDistance;
		public TextView tvTadaID;
	}

}
