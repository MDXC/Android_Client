package com.test.juxiaohui.mdxc.adapter;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import com.test.juxiaohui.R;

import com.test.juxiaohui.mdxc.data.CityData;
import com.test.juxiaohui.mdxc.manager.CityManager;

import android.content.Context;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class NationalityStickyListHeadersListAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

	private String[] mResultCountries;
	private SparseIntArray mPositionOfSection;
	private SparseIntArray mSectionOfPosition;
	private boolean isShowResult = true;
	
	private LayoutInflater mInflater;
	
	private Context mContext;
	
	private StickyListHeadersListView mStickHeaderListView;
	
	public NationalityStickyListHeadersListAdapter(Context context, StickyListHeadersListView stickHeaderListView)
	{
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mStickHeaderListView = stickHeaderListView;
		
		mResultCountries = mContext.getResources().getStringArray(R.array.countries);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mResultCountries != null)
			return mResultCountries.length;
		return 0;
	}

	@Override
	public String getItem(int arg0) {
		// TODO Auto-generated method stub
		return mResultCountries[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = new TextView(mContext);
			//holder.description = new TextView(mContext);
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,Math.round(mContext.getResources().getDimension(R.dimen.margin_36)));
			convertView.setLayoutParams(params);
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
			
			((TextView)convertView).setTextColor(mContext.getResources().getColor(R.color.color_333333));
			((TextView)convertView).setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen.font_size_small));
			convertView.setPadding(Math.round(mContext.getResources().getDimension(R.dimen.margin_10)), 0, 0, 0);			
			//convertView.setTag(holder);	
		} 

		CharSequence country = mResultCountries[position];
		((TextView)convertView).setText(country);

		return convertView;
	}


	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = new TextView(mContext);
			//holder.description = new TextView(mContext);
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,Math.round(mContext.getResources().getDimension(R.dimen.margin_36)));
			convertView.setLayoutParams(params);
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.color_gray_9));
			
			((TextView)convertView).setTextColor(mContext.getResources().getColor(R.color.color_333333));
			((TextView)convertView).setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen.font_size_small));
			convertView.setPadding(Math.round(mContext.getResources().getDimension(R.dimen.margin_10)), 0, 0, 0);			
			//convertView.setTag(holder);	
		} 

		CharSequence headerChar = mResultCountries[position].subSequence(0, 1);
		((TextView)convertView).setText(headerChar);

		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		return mResultCountries[position].subSequence(0, 1).charAt(0);
	}
	
	public String getDataByPosition(int position)
	{
		return getItem(position);
	}

	public int getPositionForSection(int section) {
		return mPositionOfSection.get(section);
	}

	public int getSectionForPosition(int position) {
		return mSectionOfPosition.get(position);
	}

	@Override
	public Object[] getSections() {
		mPositionOfSection = new SparseIntArray();
		mSectionOfPosition = new SparseIntArray();
		int count = getCount();
		List<String> list = new ArrayList<String>();
		list.add(mContext.getString(R.string.search_header));
		mPositionOfSection.put(0, 0);
		mSectionOfPosition.put(0, 0);
		for (int i = 0; i < count; i++) {

			String letter = getItem(i).substring(0, 1);
			int section = list.size()-1;
			if (list.get(section) != null && !list.get(section).equals(letter)) {
				list.add(letter);
				section++;
				mPositionOfSection.put(section, i);
			}
			mSectionOfPosition.put(i, section);
		}
		return list.toArray(new String[list.size()]);
	}
	
	public void setFilter(String filter)
	{
		mResultCountries = getSearchResult(filter);
		this.notifyDataSetChanged();
	}
	
	private String[] getSearchResult(String condition)
	{
		String[] temp = mContext.getResources().getStringArray(R.array.countries);
		   if(condition==null||condition.length()==0)
	        {
	            return temp;
	        }
	        else
	        {
	        	ArrayList<String> result = new ArrayList<String>();
	        //	int index = 0;
	            for(int i=0; i<temp.length; i++)
	            {
	            	if(isAStartWithBIgnoreCase(temp[i],condition))
	            	{
	            		result.add(temp[i]);
	          //  		index ++;
	            	}
	            }
	            
	            return (result.toArray(new String[result.size()]));
	        }
	}

	private boolean isAStartWithBIgnoreCase(String a,String b)
	{
		if(b == null)
			return true;
		for(int i = 0; i < b.length(); i++)
		{
			if(!b.substring(i, i+1).equalsIgnoreCase(a.substring(i,i+1)))
			{
				return false;
			}
		}
		return true;
	}

}
