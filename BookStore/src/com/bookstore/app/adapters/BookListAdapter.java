package com.bookstore.app.adapters;

import java.util.ArrayList;
import java.util.List;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.bookstore.app.activity.R;
import com.bookstore.app.entities.BookEntity;
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

public class BookListAdapter extends ArrayAdapter<BookEntity> {

	BookEntity bookEntity;
	ArrayList<BookEntity> list;
	Context context;
	
	ImageOptions imgOptions;
	ImageLoader imageLoader;
	private AQuery aq;

	public BookListAdapter(Context _context, int textViewResourceId,
			List<BookEntity> objects) {
		super(_context, textViewResourceId, objects);
		context=_context;
		list = (ArrayList<BookEntity>) objects;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public BookEntity getItem(int position) {
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
		bookEntity=list.get(position);
		holder=new ViewHolder();
		
		try{
			if( convertView==null){
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				jobView = inflater.inflate(R.layout.book_list_item, null);
				
				holder.ivBookImage = (ImageView) jobView.findViewById(R.id.ivBookImage);
				holder.pbImagePreLoad = (ProgressBar) jobView.findViewById(R.id.pbImagePreLoad);
				holder.tvBookName = (TextView) jobView.findViewById(R.id.tvBookName);
				holder.tvWritterName = (TextView) jobView.findViewById(R.id.tvWriterName);
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
			
			holder.tvBookName.setText(bookEntity.full_name);
			holder.tvWritterName.setText(bookEntity.Auther_name);
			holder.tvNumberOfBook.setText(""+bookEntity.avaible);
			
			aq.id(holder.ivBookImage).image(context.getResources().getDrawable(R.drawable.ic_add_book));
			
			/*if(bookEntity.pic_url != null){
				aq.id(holder.ivBookImage).image(context.getResources().getDrawable(R.drawable.ic_add_book));
			}else{
				aq.id(holder.ivBookImage).image((CommonUrls.getInstance().IMAGE_BASE_URL+bookEntity.pic_url.toString()),imgOptions);
			}*/
			
		}catch(Exception ex){
			CommonTasks.showLogs(context, ex.getMessage());
		}
		return jobView;
	}

	public class ViewHolder {
		public TextView tvBookName;
		public TextView tvWritterName;
		public TextView tvNumberOfBook;
		public ImageView ivBookImage;
		public ProgressBar pbImagePreLoad;
	}
}
