package com.test.juxiaohui.mdxc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;
import com.test.juxiaohui.common.data.User;
import com.test.juxiaohui.mdxc.data.*;
import com.test.juxiaohui.mdxc.manager.UserManager;
import com.test.juxiaohui.mdxc.manager.UtilManager;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.common.dal.IFlightServer;
import com.test.juxiaohui.utils.SyncHTTPCaller;

/**
 * Created by yihao on 15/3/31.
 */
public class FlightServer implements IFlightServer {

	private static boolean isTestMode = true;
	private List<FlightData> mFlightDataList;
	
	/**
	 * 查询航班信息，区分国内航班与国际航班 请求示例
	 * http://64.251.7.148/flight/list?from=LAX&to=SHA&departDate
	 * =2015/04/14&returnDate=2015/04/24&cabin=Economy
	 * 
	 * @param request
	 * @param type
	 *            BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
	 * @return
	 */
	@Override
	public List<FlightData> flightSearch(final FlightSearchRequest request,
			int type) {
		String url = "http://64.251.7.148/flight/list.json?";
		url += "from=" + request.mDepartCode;
		url += "&to=" + request.mArrivalCode;
		url += "&departDate=" + request.mDepartDate;
		if (request.mTripType == FlightOrder.TRIP_ROUND) {
			url += "&returnDate=" + request.mReturnDate;
		}

		url += "&cabin=" + request.mClassType;
		SyncHTTPCaller<List<FlightData>> caller;
		caller = new SyncHTTPCaller<List<FlightData>>(url) {

			@Override
			public List<FlightData> postExcute(String result) {
				List<FlightData> resultObjects = new ArrayList<FlightData>();
				try {
					float rate = -1;
					JSONObject json = new JSONObject(result);
					//toAirports
					JSONObject toAirports = json.getJSONObject("toAirports");
					//flights

					//cities
					JSONObject cities = json.getJSONObject("cities");

					//fromAirports
					JSONObject fromAirports = json.getJSONObject("fromAirports");

					//airlines
					JSONObject airlines = json.getJSONObject("airlines");

					//prices
					JSONArray prices = json.getJSONArray("prices");
					JSONObject flights = json.getJSONObject("flights");
					for (int i = 0; i < prices.length(); i++) {
						JSONObject priceObj = prices.getJSONObject(i);
						FlightData flightData = new FlightData();
						flightData.mPrice = new PriceData();

						flightData.mPrice.mCurrency = priceObj.getString("currency");
						if (rate == -1) {
							rate = UtilManager.getInstance().getExchangeRate(flightData.mPrice.mCurrency, UtilManager.getInstance().getCurrency());
						}
						flightData.mPrice.mTicketPrice = Float.valueOf(priceObj.getString("price")) * rate;
						flightData.mPrice.mTax = Float.valueOf(priceObj.getString("taxes"));

						JSONArray fromNumbers = priceObj.getJSONArray("fromNumbers");
						int fromNumbersLength = fromNumbers.length();
						for (int j = 0; j < fromNumbersLength; j++) {
							String fromNumber = fromNumbers.getString(j);
							JSONObject flight = flights.getJSONObject(fromNumber);

							if (flight.getString("fromCity").trim().equals(request.mDepartCode)) {
								if (j > 1) {
									flightData = new FlightData(flightData);
								} 
								RouteData routeData = RouteData.fromJSON(flight, fromAirports, toAirports, cities, airlines);
								routeData.mCabinType = priceObj.getString("fromCabin");
								if(fromNumbersLength == 1) {
									flightData.mDurTime =  routeData.mDurTime;
								}
								flightData.mRoutes.add(routeData);
							} else {
								RouteData routeData = RouteData.fromJSON(flight, fromAirports, toAirports, cities, airlines);
								flightData.mRoutes.add(routeData);
								Log.d("flightData.mRoutes", flightData.mRoutes.getFirst().mArrivalCityName + " " + flightData.mRoutes.getLast().mDepartCityName);
//								if (flight.getString("toCity").trim().equals(request.mArrivalCode)) {
//									for(RouteData route:flightData.mRoutes) {
//										flightData.mDurTime += route.mDurTime;
//									}
									
//								}
								if(fromNumbersLength > 1){
									resultObjects.add(flightData);
								}
							}
				
							if(fromNumbersLength == 1){
								resultObjects.add(0,flightData);
							}
							
						}

//						JSONArray fromNumbers = priceObj.getJSONArray("fromNumbers");
//						for (int j = 0; j < fromNumbers.length(); j++) {
//							String fromNumber = fromNumbers.getString(j);
//							JSONObject flight = flights.getJSONObject(fromNumber);
//
//							if (flight.getString("fromCity").trim().equals(request.mDepartCode)) {
//								if (j > 1) {
//									flightData = new FlightData(flightData);
//								}
//								RouteData routeData = RouteData.fromJSON(flight, fromAirports, toAirports, cities, airlines);
//								routeData.mCabinType = priceObj.getString("fromCabin");
//								flightData.mRoutes.add(routeData);
//							} else {
//								RouteData routeData = RouteData.fromJSON(flight, fromAirports, toAirports, cities, airlines);
//								flightData.mRoutes.add(routeData);
//								if (flight.getString("toCity").trim().equals(request.mArrivalCode)) {
//									for(RouteData route:flightData.mRoutes){
//										flightData.mDurTime += route.mDurTime;
//									}
//									resultObjects.add(flightData);
//								}
//							}
//						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.e("FlightServer", "resultObjects size = " + resultObjects.size());
				return resultObjects;
			}
		};
		mFlightDataList = caller.execute();
		return mFlightDataList;
	}

	/**
	 * 仓位验证
	 * 
	 * @param data
	 * @param type
	 *            BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
	 * @return
	 */
	@Override
	public FlightData bookabilityRequest(FlightData data,
			int type) {
		return null;
	}

	/**
	 * 价格验证
	 * 
	 * @param data
	 * @param type
	 *            BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
	 * @return
	 */
	@Override
	public FlightData repricingRequest(FlightData data,
			int type) {
		return null;
	}

	/**
	 * 生成订单
	 *
	 * @param data
	 * @param type BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
	 * @return
	 */
	@Override
	public FlightData createOrder(FlightData data, int type) {
		return null;
	}


	/**
	 * 提交订单
	 *
	 * @param order
	 * @return 如果提交订单成功，则返回一个有效的id，否则返回""
	 */
	@Override
	public String submitOrder(FlightOrder order) {

		String url = "http://www.bookingmin.com/orders/new?";
		ContactUser contactUser = UserManager.getInstance().getContactUser();
		List params = new ArrayList();
		params.add(new BasicNameValuePair("userId", UserManager.getInstance().getCurrentUser().getId()));
		params.add(new BasicNameValuePair("tripType", order.mTripType + ""));
		//接口调用源
		//* order source, 0:websit,10: mobile explorer,11:android app,12:ios app,3:others
//		&remarks=测试
//				&baggages=测试
//				&reroute=No
//				&refund=NO
//				&expression=测试
//				&surcharge=100
//				&taxes=100
//				&publishPrice=100
//				&currency=RMB
		params.add(new BasicNameValuePair("adults", "2"));
		params.add(new BasicNameValuePair("remarks", "test"));
		params.add(new BasicNameValuePair("baggages", "test"));
		params.add(new BasicNameValuePair("reroute", "No"));
		params.add(new BasicNameValuePair("refund", "NO"));
		params.add(new BasicNameValuePair("expression", "test"));
		params.add(new BasicNameValuePair("surcharge", "100"));
		params.add(new BasicNameValuePair("taxes", "100"));
		params.add(new BasicNameValuePair("publishPrice", "100"));
		params.add(new BasicNameValuePair("currency", "USD"));
		params.add(new BasicNameValuePair("amount", ""+order.getAmount()));
		params.add(new BasicNameValuePair("source", "11"));
		params.add(new BasicNameValuePair("contactName", "yihao"));//contactUser.contactName));
		params.add(new BasicNameValuePair("contCountryCode", "86"));//contactUser.contCountryCode));
		params.add(new BasicNameValuePair("contPhone", "15510472558"));//contactUser.contPhone));
		params.add(new BasicNameValuePair("contEmail", "yihao@qq.com"));//contactUser.contEmail));
		params.add(new BasicNameValuePair("recipient", "zhuxinze"));
		params.add(new BasicNameValuePair("reciPhone", "13466718731"));
		params.add(new BasicNameValuePair("reciAddress", "中国"));
		params.add(new BasicNameValuePair("reciPostalCode", "10086"));
		params.add(new BasicNameValuePair("pickUpTime", "2015-05-01"));
		params.add(new BasicNameValuePair("specialReq", "test"));
		params.add(new BasicNameValuePair("receiveTime", "2015-05-01"));

		params.add(new BasicNameValuePair("trips", FlightData.convertToOrderParams(order.mFlightdataList).toString()));

		if(isTestMode)
		{
			List<Passenger> listPassengers = new ArrayList<Passenger>();
			listPassengers.add(Passenger.createTestPassenger());
			listPassengers.add(Passenger.createTestPassenger());
			params.add(new BasicNameValuePair("passengers", Passenger.converToOrderParams(listPassengers).toString()));
		}
		else
		{
			params.add(new BasicNameValuePair("passengers", Passenger.converToOrderParams(order.mListPassenger).toString()));
		}
		Log.v(DemoApplication.TAG, params.toString());
		SyncHTTPCaller<String> caller;
		caller = new SyncHTTPCaller<String>(url, "", params) {

			@Override
			public String postExcute(String result) {
				String resultObj = null;
				if(result.contains("error")){
					return  "fail";
				}
				else{
					return "success";
				}

			}
		};
		return caller.execute();
	}

	/**
	 * 取消订单
	 *
	 * @param id 订单id
	 * @return 执行结果 CANCEL_SUCCESS 或者是CANCEL_FAILED
	 */
	@Override
	public String cancelOrder(String id) {
		String url = "http://www.bookingmin.com/orders/cancel?id="+id;
		SyncHTTPCaller<String> caller = new SyncHTTPCaller<String>(url, null, null, SyncHTTPCaller.TYPE_GET) {
			@Override
			public String postExcute(String result) {
				if(result.equals("success"))
				{
					return IFlightServer.CANCEL_SUCCESS;
				}
				else{
					return IFlightServer.CANCEL_FAILED;
				}
			}
		};

		return caller.execute();
	}

	/**
	 * 订单列表查询
	 *
	 * @param user_id 用户id
	 * @return 当前用户的机票订单列表
	 */
	@Override
	public List<FlightOrder> queryOrderList(String user_id) {
		String url = "http://www.bookingmin.com/orders/user/"+user_id;
		SyncHTTPCaller<List<FlightOrder>> caller = new SyncHTTPCaller<List<FlightOrder>>(url, null, null, SyncHTTPCaller.TYPE_GET) {
			@Override
			public List<FlightOrder> postExcute(String result) {
				List<FlightOrder> listOrders = new ArrayList<FlightOrder>();
				try {
					JSONArray jsonArray = new JSONArray(result);
					for(int i=0; i<jsonArray.length(); i++){
						FlightOrder order = FlightOrder.fromJSON(jsonArray.getJSONObject(i));
						listOrders.add(order);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return listOrders;
			}
		};

		return caller.execute();
	}


	/**
	 * 订单详情查询
	 * 
	 * @param data
	 * @param type
	 *            BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
	 * @return
	 */
	@Override
	public FlightData queryOrderDetail(FlightData data,
			int type) {
		return null;
	}

	/**
	 * 获取某一条航班数据
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public FlightData getFlightData(String id) {
		if(id == null)
			return FlightData.NULL;
		for(FlightData data:mFlightDataList)
		{
			if(id.equalsIgnoreCase(data.getId()))
				return data;
		}
		return FlightData.NULL;
	}

	private String createFromFile() {

		try {
			InputStream is = DemoApplication.applicationContext.getAssets()
					.open("testflight.txt");
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			char buffer[] = new char[is.available()];
			br.read(buffer);
			String result = new String(buffer);
			is.close();
			br.close();
			isr.close();
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
