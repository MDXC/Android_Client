package com.test.juxiaohui.mdxc.adapter;

import java.util.ArrayList;
import java.util.List;

import com.test.juxiaohui.mdxc.data.CountryCode;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.manager.CountryManager;

public class ControlCodeListAdapter extends BaseAdapter  implements StickyListHeadersAdapter, SectionIndexer{

	private ArrayList<CountryCode> mResultControl;
	private ArrayList<CountryCode> mNearbyPorts;
	private ArrayList<CountryCode> mLastSearchCities;
	private ArrayList<CountryCode> mHotCities;
	private SparseIntArray mPositionOfSection;
	private SparseIntArray mSectionOfPosition;
	private boolean isShowResult = true;
	
	private LayoutInflater mInflater;
	
	private Context mContext;
	private StickyListHeadersListView mStickHeaderListView;

    
    
	public ControlCodeListAdapter(ArrayList<CountryCode> nearbyPorts, ArrayList<CountryCode> lastSearchControl, ArrayList<CountryCode> hotControls,Context context, StickyListHeadersListView stickHeaderListView){
		mContext = context;
		mResultControl = CountryManager.getInstance().getSearchResult("");
		
		mHotCities = hotControls;
		mNearbyPorts = nearbyPorts;
		mLastSearchCities = lastSearchControl;
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mStickHeaderListView = stickHeaderListView;
	}
	

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		CountryCode mControl = (CountryCode)getItem(position);
//		if(mControl == null && ! (mControl instanceof CountryCode)){
//			return null;
//		} else {
//			convertView = View.inflate(mContext, R.layout.controlcode_listviewitem, null);
//			
//			TextView mControlEngNameView = (TextView)convertView.findViewById(R.id.controlEngName);
//			TextView mControlChinaNameView = (TextView)convertView.findViewById(R.id.controlChinaName);
//			TextView mControlCodeView = (TextView)convertView.findViewById(R.id.controlCode);
//			if (mControl.mControlEngName.length() > 15) {
//				if(mControl.mControlshortName.trim().length() > 0){
//					mControlEngNameView.setText(mControl.mControlshortName);
//				} else {
//					String replaceEngName = mControl.mControlEngName.substring(0,7)+"...";
//					mControlEngNameView.setText(replaceEngName);
//				}
//			} else {
//				
//				mControlEngNameView.setText(mControl.mControlEngName);
//			}
//			mControlChinaNameView.setText(mControl.mControlChinaName);
//			mControlCodeView.setText(mControl.mControlCode);
//			return convertView;
//		}
// 	}
	
	
	
	
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		if(isShowResult)
			mStickHeaderListView.headerVisibable = false;
		else
			mStickHeaderListView.headerVisibable = true;
		
		if(isShowResult && mResultControl != null)
			return mResultControl.size();
		else if(!isShowResult)
		{
			int a = mNearbyPorts == null ? 0 : mNearbyPorts.size();
			int b = mLastSearchCities == null ? 0 : mLastSearchCities.size();
			int c = mHotCities == null ? 0 : mHotCities.size();
			return a + b + c;
		}
		else
			return 0;
	}

	@Override
	public CountryCode getItem(int arg0) {
		// TODO Auto-generated method stub
		if(isShowResult && mResultControl != null)
			return mResultControl.get(arg0);
		else if(!isShowResult)
		{
			int a = mNearbyPorts == null ? 0 : mNearbyPorts.size();
			int b = mLastSearchCities == null ? 0 : mLastSearchCities.size();
			int c = mHotCities == null ? 0 : mHotCities.size();
			
			if(arg0 >= 0 && arg0 < a)
			{
				return mNearbyPorts.get(arg0);
			}
			if(arg0 >= a && arg0 < a + b)
			{
				return mLastSearchCities.get(arg0 - a);
			}
			if(arg0 >= a + b && arg0 < a + b + c)
			{
				return mHotCities.get(arg0 - a - b);
			}
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ItemViewHoler holder;
		CountryCode data;
	//	try
	//	{
			data = (CountryCode)getItem(position);
	//	}
	//	catch(Exception e)
	//	{
			
	//	}
		if(data == null || ! (data instanceof CountryCode))
			return null;
		if (convertView == null) {
			holder = new ItemViewHoler();
			convertView = mInflater.inflate(R.layout.view_city_item,parent, false);
			holder.title =  (TextView) convertView.findViewById(R.id.tv_title);
			holder.subTitle = (TextView) convertView.findViewById(R.id.tv_subtitle);
			holder.distance = (TextView) convertView.findViewById(R.id.tv_distance);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ItemViewHoler) convertView.getTag();
		}
 
		holder.title.setText(data.mEngName);
		holder.subTitle.setText(data.mChinaName + " - (" + data.mCode + ")");
		 
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		// TODO Auto-generated method stub`
		
		if(isShowResult)
			return -1;
		
		int a = mNearbyPorts == null ? 0 : mNearbyPorts.size();
		int b = mLastSearchCities == null ? 0 : mLastSearchCities.size();
		int c = mHotCities == null ? 0 : mHotCities.size();
		if(position >= 0 && position < a)
		{
			return 0;
		}
		if(position >= a && position < a + b)
		{
			return 1;
		}
		if(position >= a + b && position < a + b + c)
		{
			return 2;
		}
		return position;
		//return 0;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		HeaderViewHolder holder;

		if(isShowResult)
			return null;
		
		if (convertView == null) {
			convertView = new TextView(mContext);
			//holder.description = new TextView(mContext);
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,Math.round(mContext.getResources().getDimension(R.dimen.margin_36)));
			convertView.setLayoutParams(params);
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.color_gray_9));
			
			((TextView)convertView).setTextColor(mContext.getResources().getColor(R.color.color_333333));
			((TextView)convertView).setTextSize(mContext.getResources().getDimension(R.dimen.font_size_small));
			convertView.setPadding(Math.round(mContext.getResources().getDimension(R.dimen.margin_10)), 0, 0, 0);
			//convertView.setTag(holder);
			
		} /*else {
			holder = (HeaderViewHolder) convertView.getTag();
		}
*/
	
		switch((int)(getHeaderId(position)))
		{
		case 0:
			((TextView)convertView).setText(mContext.getText(R.string.nearby_airports));
			break;
		case 1:
			((TextView)convertView).setText(mContext.getText(R.string.recently_searched));
			break;
		case 2:
			((TextView)convertView).setText(mContext.getText(R.string.hot_destination));
			break;
		}
		
		return convertView;
	}
	
	class ItemViewHoler
	{
		TextView title;
		TextView subTitle;
		TextView distance;
	}
	
	class HeaderViewHolder
	{
		TextView description;
	}
	
	public CountryCode getDataByPosition(int position)
	{
		return (CountryCode)getItem(position);
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

			String letter = getItem(i).mEngName.substring(0, 1);
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
		mResultControl = CountryManager.getInstance().getSearchResult(filter);
		this.notifyDataSetChanged();
	}
	

}
