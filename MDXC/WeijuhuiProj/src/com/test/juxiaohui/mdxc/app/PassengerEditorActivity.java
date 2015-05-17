package com.test.juxiaohui.mdxc.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.app.view.CabinClassDialog;
import com.test.juxiaohui.mdxc.app.view.PassengerGenderDialog;
import com.test.juxiaohui.mdxc.app.view.PassengerIDTypeDialog;
import com.test.juxiaohui.mdxc.data.FlightSearchRequest;
import com.test.juxiaohui.mdxc.data.Passenger;
import com.test.juxiaohui.mdxc.manager.UserManager;
import com.test.juxiaohui.mdxc.mediator.IPassengerEditorMediator;
import com.test.juxiaohui.mdxc.mediator.IPassengerListMediator;
import com.test.juxiaohui.mdxc.widget.CommonTitleBar;
import com.test.juxiaohui.widget.CalendarActivity;
import com.test.juxiaohui.widget.CalendarActivity.onDataSelectedListener;

public class PassengerEditorActivity extends Activity implements
		IPassengerEditorMediator {
	
	private Context mContext;
	private LayoutInflater mInflater;
	
	private LinearLayout mMainView;
	private CommonTitleBar mTitleBar;
	private RelativeLayout mContentView;
	private ScrollView mScrollView;
	
	private EditText mEdtName,mEdtIDNumber;
	private RelativeLayout mIDTypeLayout,mNationalityLayout,mBirthLayout,mGenderLayout;
	private TextView mTvIDType,mTvNationality,mTvBirth,mTvGender;
	
	
	private Passenger mPassenger;
	
	private int mEditorType;
	
	private Button mBtnConfirm;
	
	private PassengerGenderDialog mPassengerGenderDialog;
	private PassengerIDTypeDialog mPassengerIDTypeDialog;
	
	public final static int NATIONALITY = 0;
	
	private SimpleDateFormat mDataFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//设置edittext自动聚焦时不弹出键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
		mContext = this;
		mInflater = getLayoutInflater();
		initData();
		initView();
	}
	
	private void initData()
	{
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null)
			mPassenger = (Passenger) bundle.getSerializable("passenger");
		if(mPassenger == null)
		{
			mEditorType = CREATEPASSENGER;
			mPassenger = Passenger.NULL;
		}
		else
			mEditorType = MODIFYPASSENGER;
	}
	
	private void initView()
	{
		mMainView = new LinearLayout(mContext);
		mMainView.setOrientation(LinearLayout.VERTICAL);
		mTitleBar = new CommonTitleBar(mContext);
		TextView titleTextView = new TextView(mContext);
		titleTextView.setGravity(Gravity.CENTER);
		titleTextView.setTextColor(getResources().getColor(R.color.white));
		titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size_36));
		titleTextView.setText("Passenger info");
		titleTextView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mTitleBar.setMarkLayout(titleTextView);
		
		mTitleBar.setBackIconListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mMainView.addView(mTitleBar);
		
		mScrollView = (ScrollView) mInflater.inflate(R.layout.activity_passenger_editor, null);
		mContentView = (RelativeLayout) mScrollView.findViewById(R.id.passenger_editor_view);
		addNameView();
		addPassengerIdView();
		addBasicInformationView();
		addConfirmView();
		mMainView.addView(mScrollView);
		setContentView(mMainView);
	}


	@Override
	public void addConfirmView() {
		// TODO Auto-generated method stub
		
		mBtnConfirm = (Button) mContentView.findViewById(R.id.btn_confirm);
		
		mBtnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mPassenger.mName = mEdtName.getText().toString();
				mPassenger.mIdNo = mEdtIDNumber.getText().toString();
				insertOrReplacePassenger(mPassenger);	
				setResult(IPassengerListMediator.UpdateData);
				finish();
			}
		});

	}

	@Override
	public void insertOrReplacePassenger(Passenger passenger) 
	{
		// TODO Auto-generated method stub
	
		List<Passenger> list = new ArrayList<Passenger>();
		for(Passenger data : UserManager.getInstance().getPassengerList())
		{
			Passenger temp = new Passenger();
			Passenger.clonePassenger(data, temp);
			list.add(temp);
		}
		
		if(mEditorType == CREATEPASSENGER || list.size() == 0)
		{
			passenger.mId = list == null?"1":String.valueOf(list.size()+1);
		}
		
		if(list.size() == 0)
		{
			list.add(passenger);
			UserManager.getInstance().setPassengerList(list);
		}
		else
		{
			boolean exit = false;
			for(Passenger data:list)
			{
				if(data.mId.equalsIgnoreCase(passenger.mId))
				{
					list.remove(data);
					list.add(passenger);
					exit = true;
					break;
				}
			}
			if(!exit)
			{
				list.add(passenger);
				UserManager.getInstance().setPassengerList(list);
			}
			
		}

	}

	@Override
	public void addNameView() {
		// TODO Auto-generated method stub
		mEdtName = (EditText) mContentView.findViewById(R.id.edt_name);

		if(mPassenger != Passenger.NULL)
		{
			mEdtName.setText(mPassenger.mName);
		}
		
	}

	@Override
	public void addPassengerIdView() {
		// TODO Auto-generated method stub
		mEdtIDNumber = (EditText) mContentView.findViewById(R.id.edt_id_number);
		mTvIDType = (TextView) mContentView.findViewById(R.id.tv_id_type);
		mIDTypeLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_ID_type);
		if(mPassenger != Passenger.NULL)
		{
			mEdtIDNumber.setText(mPassenger.mIdNo);
		}
		
		if(mPassenger.mIdType.equals(Passenger.ID_TYPE_ID))
		{
			mTvIDType.setText(R.string.identity_card_ID);
		}
		else if(mPassenger.mIdType.equals(Passenger.ID_TYPE_PASSPORT)){
			mTvIDType.setText(R.string.passport);
		}
		
		if(mPassengerIDTypeDialog == null)
			mPassengerIDTypeDialog = new PassengerIDTypeDialog(mContext, new IPassengerIDTypeListener() {
				
				@Override
				public void onChangePassengerIDType(int tag) {
					// TODO Auto-generated method stub
					switch(tag)
					{
						case PassengerIDTypeDialog.PASSPORT:
							mTvIDType.setText(mContext.getResources().getString(R.string.passport));
							mPassenger.mIdType = Passenger.ID_TYPE_PASSPORT;
							break;
						case PassengerIDTypeDialog.IDENTIFY:
							mTvIDType.setText(mContext.getResources().getString(R.string.identity_card_ID));
							mPassenger.mIdType = Passenger.ID_TYPE_ID;
							break;
					}
				}
			});
		
		mIDTypeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(Passenger.ID_TYPE_PASSPORT.equalsIgnoreCase(mPassenger.mIdType))
					mPassengerIDTypeDialog.show(PassengerIDTypeDialog.PASSPORT);
				else
					mPassengerIDTypeDialog.show(PassengerIDTypeDialog.IDENTIFY);
			}
		});
		
	}

	@Override
	public void addBasicInformationView() {
		// TODO Auto-generated method stub
		
		//nationality
		mTvNationality = (TextView) mContentView.findViewById(R.id.tv_nationality);
		mNationalityLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_nationality);
		mNationalityLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,NationalitySearchActivity.class);
				//startActivity(intent);
				startActivityForResult(intent, NATIONALITY);
			}
		});
		
		//brith
		mTvBirth = (TextView) mContentView.findViewById(R.id.tv_birth);
		mBirthLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_birth);
		mBirthLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openCalendar();
			}
		});
		
		//gender
		mTvGender = (TextView) mContentView.findViewById(R.id.tv_gender);
		mGenderLayout = (RelativeLayout) mContentView.findViewById(R.id.layout_gender);
		if("0".equalsIgnoreCase(mPassenger.mGender))
		{
			mTvGender.setText(R.string.male);
		}
		else if("1".equalsIgnoreCase(mPassenger.mGender)){
			mTvGender.setText(R.string.female);
		}
		if(mPassengerGenderDialog == null)
			mPassengerGenderDialog = new PassengerGenderDialog(mContext, new IPassengerGenderListener() {
				
				@Override
				public void onChangePassengerGender(int tag) {
					// TODO Auto-generated method stub
					switch(tag)
					{
						case PassengerGenderDialog.MALE:
							mTvGender.setText(mContext.getResources().getString(R.string.male));
							mPassenger.mGender = "0";
							break;
						case PassengerGenderDialog.FEMALE:
							mTvGender.setText(mContext.getResources().getString(R.string.female));
							mPassenger.mGender = "1";
							break;
					}
				}
			});
		
		mGenderLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if("0".equalsIgnoreCase(mPassenger.mGender))
					mPassengerGenderDialog.show(PassengerIDTypeDialog.PASSPORT);
				else if("1".equalsIgnoreCase(mPassenger.mGender))
					mPassengerGenderDialog.show(PassengerIDTypeDialog.IDENTIFY);
			}
		});
	}
	
	public interface IPassengerIDTypeListener
	{
		public void onChangePassengerIDType(int tag);;
	};
	
	public interface IPassengerGenderListener
	{
		public void onChangePassengerGender(int tag);
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode)
		{
		case NATIONALITY:
			if(data != null)
			{
				String n = data.getStringExtra("nationality");
				mTvNationality.setText(n);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void openCalendar() {
		// TODO Auto-generated method stub
		CalendarActivity.PopupWindows popwindow = new CalendarActivity.PopupWindows(this, getWindow().getDecorView());
    	popwindow.setDateSelectedListener(new onDataSelectedListener() {

            @Override
            public void onDateSelected(final String str_date) {
                // TODO Auto-generated method stub

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            final Date date = mDataFormat.parse(str_date);
                            String sDate = mDataFormat.format(date);
                            mTvBirth.setText(sDate);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
		
	}

}
