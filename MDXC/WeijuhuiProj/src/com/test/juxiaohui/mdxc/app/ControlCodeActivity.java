package com.test.juxiaohui.mdxc.app;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.adapter.CityStickyListHeadersListAdapter;
import com.test.juxiaohui.mdxc.adapter.ControlCodeListAdapter;
import com.test.juxiaohui.mdxc.data.ControlCode;
import com.test.juxiaohui.mdxc.manager.ControlManager;
import com.test.juxiaohui.mdxc.mediator.IControlSearchActivityMediator;
import com.test.juxiaohui.widget.Sidebar;

public class ControlCodeActivity extends Activity implements IControlSearchActivityMediator{

	
	private StickyListHeadersListView mControlListView;
	private RelativeLayout mSearchView;
	private EditText mEdtControl;
	private ImageView mImgClear;
	private ControlCodeListAdapter mControlCodeListAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
		initView();
	}
	
	private void initData(){
		ControlManager.getInstance();
	}
	
	private void initView()
	{
		this.setContentView(R.layout.activity_control);
		addSearchView();
		addControlList();
	}

	@Override
	public void addSearchView() {
		// TODO Auto-generated method stub
		mSearchView = (RelativeLayout) findViewById(R.id.view_search_bar_control);
		mEdtControl = (EditText) mSearchView.findViewById(R.id.et_keyword);
		mEdtControl.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				mControlCodeListAdapter.setFilter(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		mImgClear = (ImageView) mSearchView.findViewById(R.id.iv_clear);
	}

	@Override
	public void addControlList() {
		// TODO Auto-generated method stub
		mControlListView = (StickyListHeadersListView) findViewById(R.id.slv_content_control);
		Sidebar sidebar = (Sidebar)findViewById(R.id.sidebar_control);
		sidebar.setListView(mControlListView.getWrappedList());
		mControlListView.getWrappedList().setFastScrollEnabled(true);
		mControlCodeListAdapter = new ControlCodeListAdapter(getNearbyPort(), getLastSearchControl(), getHostControl(), this,mControlListView);
		mControlListView.setAdapter(mControlCodeListAdapter);
		mControlListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("control", mControlCodeListAdapter.getDataByPosition(arg2));
				setResult(0, intent);
				finish();
			}
		});
	}

	@Override
	public void realtimeSearch(String condition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<ControlCode> getNearbyPort() {
		// TODO Auto-generated method stub
		ArrayList<ControlCode> mNearbyPort = new ArrayList<ControlCode>();
		return mNearbyPort;
	}

	@Override
	public ArrayList<ControlCode> getLastSearchControl() {
		// TODO Auto-generated method stub
		ArrayList<ControlCode> mLastSearchControl = new ArrayList<ControlCode>();
		return mLastSearchControl;
	}

	@Override
	public ArrayList<ControlCode> getHostControl() {
		// TODO Auto-generated method stub
		ArrayList<ControlCode> mHostControl = new ArrayList<ControlCode>();
		return mHostControl;
	}

	@Override
	public ArrayList<ControlCode> getSearchResult(String condition) {
		// TODO Auto-generated method stub
		return null;
	}
}
