package com.test.juxiaohui.mdxc.app.view;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.app.FlightSearchActivity.ICabinClassListener;
import com.test.juxiaohui.mdxc.app.PassengerEditorActivity.IPassengerGenderListener;
import com.test.juxiaohui.mdxc.app.PassengerEditorActivity.IPassengerIDTypeListener;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class PassengerGenderDialog extends Dialog 
{

	private Context mContext;
	private IPassengerGenderListener mListener;
	
	private LinearLayout mMainView;
	private RelativeLayout mMaleLayout, mFemaleLayout;
	private ImageView mIvMaleSelected, mIvFemaleSelected;
	private LayoutInflater mInflater;
	private Button mBtnCancel;
	
	public static final int MALE = 0;
	public static final int FEMALE = 1;
	
	private static final int CANCEL = 2;

	
	private PassengerGenderDialog mSelf;
	
	private int mCurrentType;
	
	public PassengerGenderDialog(Context context,IPassengerGenderListener listener) 
	{
		// TODO Auto-generated constructor stub
		super(context,R.style.cabin_class_dialog);
		mContext = context;
		mListener = listener;	
		mSelf = this;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//window.setType(Window.FEATURE_NO_TITLE);
		initView();
	}
	

	
	private void initView()
	{
		mInflater= LayoutInflater.from(mContext);
		LayoutParams mainViewlp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);		
		mMainView = (LinearLayout) mInflater.inflate(R.layout.dialog_passenger_gender, null);
		mMainView.setLayoutParams(mainViewlp);
		mMaleLayout = (RelativeLayout) mMainView.findViewById(R.id.male_layout);
		mMaleLayout.setTag(MALE);
		mMaleLayout.setOnClickListener(mOnClickListener);
		mFemaleLayout = (RelativeLayout) mMainView.findViewById(R.id.female_layout);
		mFemaleLayout.setTag(FEMALE);
		mFemaleLayout.setOnClickListener(mOnClickListener);
		
		mIvMaleSelected = (ImageView) mMainView.findViewById(R.id.iv_male_selected);
		mIvMaleSelected.setVisibility(View.INVISIBLE);
		mIvFemaleSelected = (ImageView) mMainView.findViewById(R.id.iv_female_selected);
		mIvFemaleSelected.setVisibility(View.INVISIBLE);

		mBtnCancel = (Button) mMainView.findViewById(R.id.btn_cancle);
		mBtnCancel.setTag(CANCEL);
		mBtnCancel.setOnClickListener(mOnClickListener);

		switch(mCurrentType)
		{
		case MALE:
			mIvMaleSelected.setVisibility(View.VISIBLE);
			mIvFemaleSelected.setVisibility(View.INVISIBLE);
			break;
		case FEMALE:
			mIvMaleSelected.setVisibility(View.INVISIBLE);
			mIvFemaleSelected.setVisibility(View.VISIBLE);
			break;
		}
		
		setContentView(mMainView);
	}
	
	private int getScreenWidth()
	{
		DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
		if (dm.widthPixels >= dm.heightPixels)
		{
			return dm.heightPixels;
		}
		else
		{
			return dm.widthPixels;
		}
	}
	
    public void show(int flag)
    {
		
    	if(mIvFemaleSelected != null && mIvMaleSelected !=null)
    	{
    		switch(flag)
    		{
    		case MALE:
    			mIvMaleSelected.setVisibility(View.VISIBLE);
    			mIvFemaleSelected.setVisibility(View.INVISIBLE);
    			break;
    		case FEMALE:
    			mIvMaleSelected.setVisibility(View.INVISIBLE);
    			mIvFemaleSelected.setVisibility(View.VISIBLE);
    			break;
    		}
    		
    	}
    	else
    		mCurrentType = flag;
    	this.show();
	
    	DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.25f;
		lp.alpha = 0.95f;
		lp.width = dm.widthPixels;
		getWindow().setAttributes(lp);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		Window window = this.getWindow();
		window.getDecorView().setPadding(0, 0, 0, 0);
		window.setGravity(Gravity.BOTTOM);

    }
	
	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			switch((Integer)(arg0.getTag()))
			{
			case MALE:
				mListener.onChangePassengerGender(MALE);
				break;
			case FEMALE:
				mListener.onChangePassengerGender(FEMALE);
				break;

			case CANCEL:
				break;
			}
			
			mSelf.dismiss();
		}

	};
	
}
