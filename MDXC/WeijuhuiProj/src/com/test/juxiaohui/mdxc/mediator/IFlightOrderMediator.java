package com.test.juxiaohui.mdxc.mediator;

import com.test.juxiaohui.mdxc.data.ContactUser;
import com.test.juxiaohui.mdxc.data.FlightData;
import com.test.juxiaohui.mdxc.data.FlightOrder;
import com.test.juxiaohui.mdxc.data.Passenger;

import java.util.List;

public interface IFlightOrderMediator {
	
	public void addFlightView();
	
	public void addPriceView();

	/**
	 * 用来显示联系人
	 */
	public void addContactView();

	/**
	 * 用来显示乘客列表
	 */
	public void addPassengerView();
	
	//public void addPassenger(Passenger passenger);

	//在订单页面里只能移除乘客，或者设置全部乘客
	public void setPassengerList(List<Passenger> passengerList);

	public void removePassenger(Passenger passenger);

	public void setContact(ContactUser contactUser);

	public void submitOrder();

	public void cancel();

	public void setFlightOrder(FlightOrder order);

	public void showSubmitProgress();

	public void hideSubmitProgress();


}
