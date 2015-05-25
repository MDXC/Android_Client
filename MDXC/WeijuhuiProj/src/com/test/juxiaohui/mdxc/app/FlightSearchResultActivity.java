package com.test.juxiaohui.mdxc.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.manager.ServerManager;
import com.test.juxiaohui.mdxc.data.FlightData;
import com.test.juxiaohui.mdxc.data.FlightSearchRequest;
import com.test.juxiaohui.mdxc.manager.UserManager;
import com.test.juxiaohui.mdxc.mediator.ISearchResultMediator;
import com.test.juxiaohui.widget.CommonAdapter;
import com.test.juxiaohui.widget.IAdapterItem;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yihao on 15/3/13.
 */
public class FlightSearchResultActivity extends Activity implements ISearchResultMediator{
	
	ListView mListView;
	List<FlightData> mFlightsList = new ArrayList<FlightData>();
	FlightSearchRequest mRequest = FlightSearchRequest.NULL;
	RelativeLayout mRlprogress;
	ProgressBar mProgressBar;
	public static String INTENT_FLIGHT = "flightdata";


	public static void startActivity(int requestCode, FlightSearchRequest request, Activity activity)
	{
		Intent intent = new Intent(activity, FlightSearchResultActivity.class);
		intent.putExtra("request", FlightSearchRequest.toJSON(request).toString());
		activity.startActivityForResult(intent, requestCode);

	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flightsearchresult);
        mRlprogress = (RelativeLayout)findViewById(R.id.progressLayout);
        //setRequest(request);        
        try {
        	Intent intent = getIntent();
			mRequest = FlightSearchRequest.fromJSON(new JSONObject(intent.getStringExtra("request")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextView tvTitle = (TextView)findViewById(R.id.textView_title);
		tvTitle.setText("From " + mRequest.mDepartCity + " To " + mRequest.mArrivalCity);
		addResultView();
        search();

    }

    @Override
    public void setResult(List<FlightData> results) {
    	if(results != null)
		{
			((CommonAdapter<FlightData>)mListView.getAdapter()).setData(results);
			if(results.size()>0){
				mRlprogress.setVisibility(View.GONE);
			}
			else{
				TextView tv = (TextView) mRlprogress.findViewById(R.id.textView_progress);
				tv.setText("There is no flight!");
				ProgressBar bar = (ProgressBar) mRlprogress.findViewById(R.id.progressBar);
				bar.setVisibility(View.INVISIBLE);
			}

		}

		((CommonAdapter<FlightData>)mListView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void addResultView() {
		//mProgressView = getLayoutInflater().inflate(R.layout.layout_progress, null);
        mListView = (ListView)findViewById(R.id.listView_flight);
        mListView.setAdapter(new CommonAdapter<FlightData>(mFlightsList, new IAdapterItem<FlightData>() {

			@Override
			public View getView(FlightData data, View convertView) {
				// TODO Auto-generated method stub
				return FlightData.getItemView(FlightSearchResultActivity.this, getLayoutInflater(), convertView, data);
			}
		}));

        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectOneFlight(mFlightsList.get(position));
				
			}
		});
    }

    @Override
    public void selectOneFlight(FlightData data) {
    	 //FlightOrderActivity.startActivity(data.getId(),null, this);
		if(!UserManager.getInstance().isLogin()){
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent();
			intent.putExtra(INTENT_FLIGHT, FlightData.toJSON(data).toString());
			setResult(RESULT_OK, intent);
			finish();
		}

    }

	@Override
	public void setRequest(FlightSearchRequest request) {		
		mRequest = request;
	}

	@Override
	public void search() {
		if(mRequest != FlightSearchRequest.NULL)
		{
//			mListView.removeFooterView(mEmptyView);
//			mListView.addFooterView(mProgressView);
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					mFlightsList = ServerManager.getInstance().flightSearch(mRequest, 1);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							mRlprogress.setVisibility(View.VISIBLE);
							TextView tv = (TextView) mRlprogress.findViewById(R.id.textView_progress);
							tv.setText("Searching Flights...");
							ProgressBar bar = (ProgressBar) mRlprogress.findViewById(R.id.progressBar);
							bar.setVisibility(View.VISIBLE);
							setResult(mFlightsList);
						}
					});
				}
			});
			thread.start();

		}
		else
		{
			finish();
		}
		
	}
}