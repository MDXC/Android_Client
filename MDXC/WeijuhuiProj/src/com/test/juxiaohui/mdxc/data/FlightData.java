package com.test.juxiaohui.mdxc.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import android.widget.RelativeLayout;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.juxiaohui.mdxc.manager.UtilManager;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yihao on 15/3/13.
 */
public class FlightData implements Cloneable{

	
	public String mId = "";
	public String mNumber = "";

    public static SimpleDateFormat FORMAT_SEARCH = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat FORMAT_ORDER = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat FORMAT_FLIGHT_VIEW_TIME = new SimpleDateFormat("HH:mm");


    public LinkedList<RouteData> mRoutes = new LinkedList<RouteData>();
    public String mAirlineName = "unknown airline";
    public String mAirlineLogoUrl = "";
    public PriceData mPrice = PriceData.NULL;
    public String mFromCity = "";
    public String mToCity = "";
    public String mFromCode = "";
    public String mToCode = "";
    public Date mFromTime = new Date();
    public Date mToTime = new Date();
    public int mDurTime = 0;
    public int mTripType = FlightOrder.TRIP_ONE_WAY;


    public FlightData(FlightData flightData)
    {
        mPrice = new PriceData(flightData.mPrice);
        mFromCity = flightData.mFromCity;
        mFromCode = flightData.mFromCode;
        mToCity = flightData.mToCity;
        mToCode = flightData.mToCode;
        mFromTime = (Date)flightData.mFromTime.clone();
        mToTime = (Date)flightData.mToTime.clone();
        mDurTime = flightData.mDurTime;
        mTripType = flightData.mTripType;
    }

    public FlightData(){

    }

    public static FlightData NULL = new FlightData();

    public static FlightData createTestData() {
        try {
            InputStream is = DemoApplication.applicationContext.getAssets().open("test_flight_data.txt");
            InputStreamReader isr=new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            char []buffer = new char[is.available()];
            isr.read(buffer);
            String str = String.valueOf(buffer);

            try {
                JSONObject object = new JSONObject(str);
                return FlightData.fromJSON(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            is.close();
            br.close();
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FlightData.NULL;
    }

    public String getId() {
        mId = mNumber + mFromTime;
        return mId;
    }

    public static class ViewHolder {
        TextView mTvAirlineName;
        ImageView mIvAirlineLogo;
        TextView mTvDepartTime;
        TextView mTvArrivalTime;
        TextView mTvDepartCity;
        TextView mTvArrivalCity;
        TextView mTvDuration;
        TextView mTvCurrency;
        TextView mTvPrize;
        TextView mTvStop0;
        TextView mTvStop1;
        TextView mTvStop2;
    }

    public static FlightData fromJSON(JSONObject jsonObject) {
        FlightData flightData = new FlightData();
        try {
            flightData.mNumber = jsonObject.getString("number");
            flightData.mAirlineName = jsonObject
                    .getString("airline");
            flightData.mFromCity = jsonObject.getString("fromCity");
            flightData.mToCity = jsonObject.getString("toCity");
            flightData.mFromCode = jsonObject
                    .getString("fromAirport");
            flightData.mToCode = jsonObject.getString("toAirport");
            flightData.mFromTime = FORMAT_SEARCH.parse(jsonObject.getString("fromTime").replace("T", " "));
            flightData.mToTime = FORMAT_SEARCH.parse(jsonObject.getString("toTime").replace("T", " "));
           // flightData.mDurTime = jsonObject.getString("duration");
            if (jsonObject.has("price")) {
                flightData.mPrice = new PriceData();
                flightData.mPrice.mTicketPrice = Float
                        .valueOf(jsonObject.getString("price"));
                if (jsonObject.getString("trip_type").equals("depart")) {
                    flightData.mTripType = FlightOrder.TRIP_ONE_WAY;
                } else if (jsonObject.getString("trip_type").equals("return")) {
                    flightData.mTripType = FlightOrder.TRIP_ROUND;
                }
            }

            JSONArray jsonRoutes = jsonObject.getJSONArray("routes");
            for(int i=0; i<jsonRoutes.length(); i++){
                RouteData route = RouteData.fromJSON(jsonRoutes.getJSONObject(i));
                flightData.mRoutes.add(route);
                flightData.mDurTime += route.mDurTime;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            flightData = FlightData.NULL;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return flightData;
    }

    public static JSONObject toJSON(FlightData flightData) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("number", flightData.mNumber);
            jsonObject.put("airline", flightData.mAirlineName);
            jsonObject.put("fromCity", flightData.mFromCity);
            jsonObject.put("toCity", flightData.mToCity);
            jsonObject.put("fromAirport", flightData.mFromCode);
            jsonObject.put("toAirport", flightData.mToCode);
            jsonObject.put("fromTime", FORMAT_SEARCH.format(flightData.mFromTime));
            jsonObject.put("toTime", FORMAT_SEARCH.format(flightData.mToTime));
            jsonObject.put("duration", flightData.mDurTime);
            if (flightData.mPrice != null) {
                jsonObject.put("price", flightData.mPrice.mTicketPrice);
                if (flightData.mTripType == FlightOrder.TRIP_ONE_WAY) {
                    jsonObject.put("trip_type", "depart");
                } else if (flightData.mTripType == FlightOrder.TRIP_ROUND) {
                    jsonObject.put("trip_type", "return");
                }
            }
            JSONArray jsonRoutes = new JSONArray();
            for(RouteData data:flightData.mRoutes){
                jsonRoutes.put(RouteData.toJSON(data));
            }
            jsonObject.put("routes", jsonRoutes);

        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }
        return jsonObject;
    }

    public static View getItemView(Context context, LayoutInflater inflator, View convertView, FlightData data) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = inflator.inflate(R.layout.item_flight, null);
            holder = new ViewHolder();
            holder.mTvAirlineName = (TextView) convertView.findViewById(R.id.textView_aireline_name);
            holder.mIvAirlineLogo = (ImageView) convertView.findViewById(R.id.imageView_airline_logo);
            holder.mTvDepartTime = (TextView) convertView.findViewById(R.id.tv_depart_date);
            holder.mTvDepartCity = (TextView) convertView.findViewById(R.id.tv_depart_city);
            holder.mTvArrivalTime = (TextView) convertView.findViewById(R.id.tv_arrival_date);
            holder.mTvArrivalCity = (TextView) convertView.findViewById(R.id.tv_arrival_city);
            holder.mTvDuration = (TextView) convertView.findViewById(R.id.tv_duration);
            holder.mTvCurrency = (TextView) convertView.findViewById(R.id.tv_currency);
            holder.mTvPrize = (TextView) convertView.findViewById(R.id.tv_price);

            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        if (holder == null)
            return convertView;
        if(data.mRoutes.size()>0){
        	if (data.mRoutes.size() > 1) {
        		
                holder.mTvAirlineName.setText(data.mRoutes.getFirst().mAirlineName + " " + data.mRoutes.getFirst().mNumbers + " / " + data.mRoutes.getLast().mAirlineName + " " + data.mRoutes.getLast().mNumbers);
        	} else {
        		
                holder.mTvAirlineName.setText(data.mRoutes.getFirst().mAirlineName + " " + data.mRoutes.getFirst().mNumbers);
        	}
        }
        if (data.mAirlineLogoUrl.length() > 0) {
            holder.mIvAirlineLogo.setImageResource(new Integer(data.mAirlineLogoUrl));
        }
        if (data.mRoutes.size() >= 2) {
            View stopView = convertView.findViewById(R.id.view_stop_0);
            stopView.setVisibility(View.VISIBLE);
            holder.mTvStop0 = (TextView)stopView.findViewById(R.id.tv_stop_code);
            holder.mTvStop0.setText(data.mRoutes.getFirst().mArrivalCityName);
        }
        if (data.mRoutes.size() >= 3) {
            View stopView = convertView.findViewById(R.id.view_stop_1);
            stopView.setVisibility(View.VISIBLE);
            holder.mTvStop1 = (TextView)stopView.findViewById(R.id.tv_stop_code);
            holder.mTvStop1.setText(data.mRoutes.get(1).mArrivalCityName);
        }
        if (data.mRoutes.size() >= 4) {
            View stopView = convertView.findViewById(R.id.view_stop_2);
            stopView.setVisibility(View.VISIBLE);
            holder.mTvStop2 = (TextView)stopView.findViewById(R.id.tv_stop_code);
            holder.mTvStop2.setText(data.mRoutes.get(2).mArrivalCityName);
        } 
        if(data.mRoutes.size() == 1) {
        	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        	params.bottomMargin = (int)(20 * context.getResources().getDisplayMetrics().density);
         	holder.mTvDuration.setLayoutParams(params);
        }
        holder.mTvDepartTime.setText(FORMAT_FLIGHT_VIEW_TIME.format(data.mRoutes.getFirst().mDepartTime));
        holder.mTvDepartCity.setText(data.mRoutes.getFirst().mDepartAirportName);
        holder.mTvArrivalTime.setText(FORMAT_FLIGHT_VIEW_TIME.format(data.mRoutes.getLast().mArrivalTime));
        holder.mTvArrivalCity.setText(data.mRoutes.getLast().mArrivalAirportName);
        int dataSize = data.mRoutes.size();
        int hour = 0;
        int min = 0;
        int day = 0;
        if (dataSize > 1) {
        	int durTime = 0;
        	for (RouteData routeData:data.mRoutes) {
        		 for (RouteData mtempData:data.mRoutes) {
        			 if (mtempData.mArrivalTime == routeData.mArrivalTime) {
        				 continue;
        			 } else {
        				 long longtime = mtempData.mDepartTime.getTime() - routeData.mArrivalTime.getTime();
        				 if (longtime > 0) {
        					 durTime = (int)(longtime/1000/60) + durTime;
        				 }
        			 }
        		 }
        		durTime += routeData.mDurTime;
            }
        	 hour = durTime/60;
             min = durTime%60;
             if (hour >= 24) {
            	 day = hour/24;
            	 hour = hour%24;
             }
        } else {
        	 hour = data.mRoutes.getFirst().mDurTime/60;
             min = data.mRoutes.getFirst().mDurTime%60;
             if (hour > 24) {
            	 day = hour/24;
            	 hour = hour%24;
             }
        }
        
        if (day > 0) {
        	holder.mTvDuration.setText(day+ "day" + hour + "h" + min + "min");
        } else {
        	holder.mTvDuration.setText(hour + "h" + min + "min");
        }
       
        
        holder.mTvCurrency.setText(UtilManager.getInstance().getCurrency());
        holder.mTvPrize.setText(data.mPrice.mTicketPrice + "");

        return convertView;
    }

    public static View displayDepartTime(View view, FlightData data){
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout_depart_time);
        if(null==layout){
            return null;
        }
        else{
            layout.setVisibility(View.VISIBLE);
            TextView tv = (TextView) layout.findViewById(R.id.textView_depart_time);
            tv.setText("Depart time :" + FORMAT_FLIGHT_VIEW_TIME.format(data.mRoutes.getFirst().mDepartTime));
        }
        return view;
    }

    /**
     *
     * @param listFlightData
     * @return
     */
    public static JSONArray convertToOrderParams(List<FlightData> listFlightData) {
        Assert.assertNotNull(listFlightData);
        try {
            JSONArray array = new JSONArray();
            for (FlightData flight : listFlightData) {
                if(flight==FlightData.NULL)
                {
                    break;
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("flight", flight.mNumber);
                jsonObject.put("departTime", FORMAT_ORDER.format(flight.mFromTime));
                jsonObject.put("arrivalTime", FORMAT_ORDER.format(flight.mToTime));
                jsonObject.put("price", flight.mPrice.mTicketPrice);
                jsonObject.put("currency", flight.mPrice.mCurrency);
                jsonObject.put("fromCity", flight.mRoutes.getFirst().mDepartCityCode);
                jsonObject.put("fromAirport", flight.mRoutes.getFirst().mDepartAirportCode);
                jsonObject.put("toCity", flight.mRoutes.getLast().mArrivalCityCode);
                jsonObject.put("toAirport", flight.mRoutes.getLast().mArrivalAirportCode);
                jsonObject.put("airline", flight.mAirlineName);
                jsonObject.put("aircraft", flight.mRoutes.getFirst().mAircraft);
                jsonObject.put("clazz", "0");
                jsonObject.put("sortNo", "1");
                array.put(jsonObject);
            }
            return array;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param jsonObject
     * @return
     */
    public static FlightData fromOrderParam(JSONObject jsonObject)
    {
        FlightData flightData = new FlightData();
        try {
            flightData.mNumber = jsonObject.getString("flight");
            flightData.mFromTime = FORMAT_ORDER.parse(jsonObject.getString("departTime"));
            flightData.mToTime = FORMAT_ORDER.parse(jsonObject.getString("arrivalTime"));
            flightData.mPrice = new PriceData();
            flightData.mPrice.mTicketPrice = (float)jsonObject.getDouble("price");
            flightData.mPrice.mCurrency = jsonObject.getString("currency");
            flightData.mFromCity = jsonObject.getString("fromCity");
            flightData.mToCity = jsonObject.getString("toCity");
            flightData.mAirlineName = jsonObject.getString("airline");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flightData;
    }

}
