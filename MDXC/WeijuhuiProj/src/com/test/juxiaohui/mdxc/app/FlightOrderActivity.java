package com.test.juxiaohui.mdxc.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.data.ContactUser;
import com.test.juxiaohui.mdxc.data.FlightData;
import com.test.juxiaohui.mdxc.data.FlightOrder;
import com.test.juxiaohui.mdxc.data.Passenger;
import com.test.juxiaohui.mdxc.manager.FlightOrderManager;
import com.test.juxiaohui.mdxc.manager.ServerManager;
import com.test.juxiaohui.mdxc.data.FlightData;
import com.test.juxiaohui.mdxc.data.Passenger;
import com.test.juxiaohui.mdxc.manager.UserManager;
import com.test.juxiaohui.mdxc.mediator.IFlightOrderMediator;

public class FlightOrderActivity extends Activity implements
		IFlightOrderMediator {
	public static int REQ_SELECT_PASSENGER = 0;
	public static final int MESSAGE_SUBMIT_ORDER = 1;
	
	private TextView mTvPerAireFarePrice,mTvPerAireFareCurrency,mTvPerTaxPrice,mTvPerTaxCurrency,mTvTotalPrice;
	private ImageButton mIbAddPassenger;
	private LinearLayout mLlFlights;
	private LinearLayout mLlPassengers;
	private TextView mTvAddPassengers;
	private UserManager mUserManager = UserManager.getInstance();
	

	private LinearLayout mSelf;
	private LayoutInflater mInflater;
	private FlightOrder mFlightOrder;
	
	


	Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
				case MESSAGE_SUBMIT_ORDER:
					submitOrder();
					break;
			}
		}
	};
	public static void startActivity(String orderId, Context context)
	{
		Intent intent = new Intent(context, FlightOrderActivity.class);
		intent.putExtra("order_id", orderId);
		context.startActivity(intent);
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mInflater = this.getLayoutInflater();
		mSelf = (LinearLayout) mInflater.inflate(R.layout.activity_flight_book, null);
		setFlightOrder(FlightOrderManager.getInstance().getFlightOrderbyId(getIntent().getStringExtra("order_id")));
		setContentView(mSelf);	
		addFlightView();
		addPassengerView();
		addPriceView();

		Button btn_OK = (Button)this.findViewById(R.id.btn_bottom_submit);
		btn_OK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		       Message msg = Message.obtain(mHandler);
		       msg.what = MESSAGE_SUBMIT_ORDER;
		       UserManager.getInstance().checkLogin(FlightOrderActivity.this, msg, true);
			}
		});
	}
	
/*	@Override
	public void setFlightData(FlightData fromData,FlightData toData) {
		mFromData = fromData;
		mToData = toData;
		if(mFromData == null)
			finish();
		if(toData == null)
			isOneWay = true;
		else
			isOneWay = false;
	}*/

	@Override
	public void addFlightView() {
		mLlFlights = (LinearLayout) findViewById(R.id.ll_airlines);
		if(mFlightOrder.mTripType == FlightOrder.TRIP_ONE_WAY){
			View view = FlightData.getItemView(this, this.getLayoutInflater(), null, mFlightOrder.getStartFlightData());
			view = FlightData.displayDepartTime(view, mFlightOrder.getStartFlightData());
			mLlFlights.addView(view);
		}else if(mFlightOrder.mTripType == FlightOrder.TRIP_ROUND){
			View view;
			view = FlightData.getItemView(this, this.getLayoutInflater(), null, mFlightOrder.getStartFlightData());
			view = FlightData.displayDepartTime(view, mFlightOrder.getStartFlightData());
			mLlFlights.addView(view);

			view = FlightData.getItemView(this, this.getLayoutInflater(), null, mFlightOrder.getReturnFlightData());
			view = FlightData.displayDepartTime(view, mFlightOrder.getReturnFlightData());
			mLlFlights.addView(view);
		}
	}

	@Override
	public void addPriceView() {
		String temp;
		mTvPerAireFarePrice = null;
		mTvPerAireFareCurrency = null;
		mTvPerTaxPrice = null;
		mTvPerTaxCurrency = null;
		mTvTotalPrice = null;
		//机票单价
		FlightData fromData = mFlightOrder.getStartFlightData();
		FlightData toData = mFlightOrder.getReturnFlightData();
		mTvPerAireFarePrice = (TextView) this.findViewById(R.id.tv_per_price);
		temp = mFlightOrder.mTripType==FlightOrder.TRIP_ONE_WAY? String.valueOf(fromData.mPrice.mTicketPrice):String.valueOf(fromData.mPrice.mTicketPrice + toData.mPrice.mTicketPrice);
		mTvPerAireFarePrice.setText(temp);	
		//机票单价币种
		mTvPerAireFareCurrency = (TextView) this.findViewById(R.id.tv_per_price_currency);
		temp = String.valueOf(fromData.mPrice.mCurrency);
		mTvPerAireFareCurrency.setText(temp);
		//税费单价
		mTvPerTaxPrice = (TextView) this.findViewById(R.id.tv_tax);
		temp = mFlightOrder.mTripType==FlightOrder.TRIP_ONE_WAY? String.valueOf(fromData.mPrice.mTax):String.valueOf(fromData.mPrice.mTax + toData.mPrice.mTax);
		mTvPerTaxPrice.setText(temp);
		//税费单价币种
		mTvPerTaxCurrency = (TextView) this.findViewById(R.id.tv_tax_currency);
		temp = String.valueOf(fromData.mPrice.mCurrency);

		mTvPerTaxCurrency.setText(temp);
		//总价
		mTvTotalPrice = (TextView) mSelf.findViewById(R.id.tv_amount_with_current_currency);
		if(mFlightOrder.mTripType==FlightOrder.TRIP_ONE_WAY)
			mTvTotalPrice.setText("" + getText(R.string.USD) + (fromData.mPrice.mTicketPrice + fromData.mPrice.mTax));
		else
			mTvTotalPrice.setText("" + getText(R.string.USD) +
									(fromData.mPrice.mTicketPrice + fromData.mPrice.mTax +
											toData.mPrice.mTicketPrice + toData.mPrice.mTax));

	}

	@Override
	public void addContactView() 
	{
		
	}

	@Override
	public void addPassengerView() 
	{
	//	mIbAddPassenger = (ImageButton)findViewById(R.id.imageButton_addPassenger);
		mTvAddPassengers = (TextView)findViewById(R.id.tv_addPassenger);
		mTvAddPassengers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(UserManager.getInstance().isLogin()){
					Intent intent = new Intent(FlightOrderActivity.this, PassengerListActivity.class);
					startActivityForResult(intent, REQ_SELECT_PASSENGER);
				}
				else {

					UserManager.getInstance().sendMessageToLogin(FlightOrderActivity.this);
				}


			}
		});
		mLlPassengers = (LinearLayout) findViewById(R.id.ll_guests);

	}

	/**
	 * 设置一组乘客
	 * @param passengerList
	 */
	@Override
	public void setPassengerList(List<Passenger> passengerList) {
		LinearLayout ll = (LinearLayout) this.getLayoutInflater().inflate(R.layout.item_remove_container, null);
		ll.removeAllViews();
		for(Passenger passenger:passengerList){
			addPassenger(passenger);
		}
	}


	/**
	 * 移除一个乘客
	 * @param passenger
	 */
	@Override
	public void removePassenger(final Passenger passenger) {
		int index = mFlightOrder.mListPassenger.indexOf(passenger);
		mFlightOrder.mListPassenger.remove(passenger);
		mLlPassengers.removeViewAt(index);
	}

	@Override
	public void setContact(ContactUser contactUser) {
		mUserManager.setContactUser(contactUser);
	}

	@Override
	public void submitOrder() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				final String result = FlightOrderManager.getInstance().submitFlightOrder(mFlightOrder);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(result.equals(FlightOrderManager.SUBMIT_FAILED)){
							Toast.makeText(FlightOrderActivity.this, getString(R.string.order_submitted_failed), Toast.LENGTH_SHORT).show();
						}
						else{
							Toast.makeText(FlightOrderActivity.this, getString(R.string.order_submitted), Toast.LENGTH_SHORT).show();
							finish();
						}
					}
				});

			}
		});
		t.start();



	}

	@Override
	public void cancel() {
		finish();
	}

	@Override
	public void setFlightOrder(FlightOrder order) {
		mFlightOrder = order;
	}

	@Override
	public void showSubmitProgress() {

	}

	@Override
	public void hideSubmitProgress() {

	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == REQ_SELECT_PASSENGER)
		{
			if(data == null)
				return;
			final ArrayList<String> ids = data.getStringArrayListExtra("passenger_ids");
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					mFlightOrder.mListPassenger.clear();
					final List<Passenger> passengerList = new ArrayList<Passenger>();
					for (String id : ids) {
						 Passenger passenger = UserManager.getInstance().getPassengerById(
								id);
						passengerList.add(passenger);
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							setPassengerList(passengerList);
						}
					});
				}
			});
			t.start();
		}
	}
	
	


	private void addPassenger(final Passenger passenger) {
		mFlightOrder.mListPassenger.add(passenger);
		LinearLayout ll = (LinearLayout) this.getLayoutInflater().inflate(R.layout.item_remove_container, null);
		ImageView ivRemove = (ImageView)ll.findViewById(R.id.imageView_remove);
		ivRemove.setClickable(true);
		ivRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removePassenger(passenger);
			}
		});
		ll.addView(Passenger.getItemView(this, this.getLayoutInflater(), null, passenger),  ll.getChildCount(),
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mLlPassengers.addView(ll);
	}

}
