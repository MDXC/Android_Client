package com.test.juxiaohui.mdxc.app;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.data.Passenger;
import com.test.juxiaohui.mdxc.manager.UserManager;
import com.test.juxiaohui.mdxc.mediator.ITravelersListActivityMediator;

public class TravelersListActivity extends Activity implements ITravelersListActivityMediator,OnClickListener,OnItemClickListener{

	
	List<Passenger> passengerList = null;
	LinearLayout noDataView = null;
	ListView travelerListView = null;
	Button contactUsBtn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initContentView();
		
	}
	
	
	public void initContentView(){
		setContentView(R.layout.activity_travelerslist);
		passengerList = getTravelersList(); 
		addNoDataView();
		addTravelerListView();
		addInfoMationer();
		

		if (passengerList.size() == 0) {
			noDataView.setVisibility(View.VISIBLE);
			travelerListView.setVisibility(View.GONE);
		} else {
			noDataView.setVisibility(View.GONE);
			travelerListView.setVisibility(View.VISIBLE);
		}
		
		
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
 		
	}

	@Override
	public void onClick(View v) {
 		
	}

	@Override
	public void addTravelerListView() {
		// TODO Auto-generated method stub
		travelerListView = (ListView)findViewById(R.id.travelers_list);
		BaseTravelersAdapter adapter = new BaseTravelersAdapter(this);
		travelerListView.setAdapter(adapter);
		travelerListView.setOnItemClickListener(this);
	}

	@Override
	public void addNoDataView() {
		// TODO Auto-generated method stub
		 noDataView = (LinearLayout)findViewById(R.id.notravelerlist_view);
	}

	@Override
	public void addInfoMationer() {
		// TODO Auto-generated method stub
		contactUsBtn = (Button)findViewById(R.id.contact_us_btn);
		contactUsBtn.setOnClickListener(this);
	}

	@Override
	public List<Passenger> getTravelersList() {
		// TODO Auto-generated method stub
		return UserManager.getInstance().getPassengerList();
	}
	
	
	
	class BaseTravelersAdapter extends BaseAdapter{

		Context mContext;
		List<Passenger> list;
		BaseTravelersAdapter(Context context){
			mContext = context;
			list = getTravelersList();
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_travelers,null);
		    
		    TextView travelerNameView = (TextView)convertView.findViewById(R.id.traveler_name);
		    Passenger passenger = list.get(position);
		    travelerNameView.setText(passenger.mName);
		    
		    TextView travelerTypeView = (TextView) convertView.findViewById(R.id.traveler_type);
		    travelerTypeView.setText(passenger.mIdType);
		    
		    TextView travelerIdView = (TextView)convertView.findViewById(R.id.traveler_ID);
		    travelerIdView.setText(passenger.mIdNo);
		    return convertView;
		}
		
	}

}
