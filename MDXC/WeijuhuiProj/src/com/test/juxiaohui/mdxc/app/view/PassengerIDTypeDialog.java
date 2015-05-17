package com.test.juxiaohui.mdxc.app.view;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.app.FlightSearchActivity.ICabinClassListener;
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

public class PassengerIDTypeDialog extends Dialog 
{

	private Context mContext;
	private IPassengerIDTypeListener mListener;
	
	private LinearLayout mMainView;
	private RelativeLayout mPassportLayout, mIdentifyLayout;
	private ImageView mIvPassportSelected, mIvIdentifySelected;
	private LayoutInflater mInflater;
	private Button mBtnCancel;
	
	public static final int PASSPORT = 0;
	public static final int IDENTIFY = 1;
	
	private static final int CANCEL = 2;

	
	private PassengerIDTypeDialog mSelf;
	
	private int mCurrentType;
	
	public PassengerIDTypeDialog(Context context,IPassengerIDTypeListener listener) 
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
		mMainView = (LinearLayout) mInflater.inflate(R.layout.dialog_passenger_id_type, null);
		mMainView.setLayoutParams(mainViewlp);
		mPassportLayout = (RelativeLayout) mMainView.findViewById(R.id.passport_layout);
		mPassportLayout.setTag(PASSPORT);
		mPassportLayout.setOnClickListener(mOnClickListener);
		mIdentifyLayout = (RelativeLayout) mMainView.findViewById(R.id.identify_layout);
		mIdentifyLayout.setTag(IDENTIFY);
		mIdentifyLayout.setOnClickListener(mOnClickListener);
		
		mIvPassportSelected = (ImageView) mMainView.findViewById(R.id.iv_passport_selected);
		mIvPassportSelected.setVisibility(View.INVISIBLE);
		mIvIdentifySelected = (ImageView) mMainView.findViewById(R.id.iv_identify_selected);
		mIvIdentifySelected.setVisibility(View.INVISIBLE);

		mBtnCancel = (Button) mMainView.findViewById(R.id.btn_cancle);
		mBtnCancel.setTag(CANCEL);
		mBtnCancel.setOnClickListener(mOnClickListener);

		switch(mCurrentType)
		{
		case PASSPORT:
			mIvPassportSelected.setVisibility(View.VISIBLE);
			mIvIdentifySelected.setVisibility(View.INVISIBLE);
			break;
		case IDENTIFY:
			mIvPassportSelected.setVisibility(View.INVISIBLE);
			mIvIdentifySelected.setVisibility(View.VISIBLE);
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
		
    	if(mIvPassportSelected != null && mIvIdentifySelected !=null)
    	{
    		switch(flag)
    		{
    		case PASSPORT:
    			mIvPassportSelected.setVisibility(View.VISIBLE);
    			mIvIdentifySelected.setVisibility(View.INVISIBLE);
    			break;
    		case IDENTIFY:
    			mIvPassportSelected.setVisibility(View.INVISIBLE);
    			mIvIdentifySelected.setVisibility(View.VISIBLE);
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
			case PASSPORT:
				mListener.onChangePassengerIDType(PASSPORT);
				break;
			case IDENTIFY:
				mListener.onChangePassengerIDType(IDENTIFY);
				break;

			case CANCEL:
				break;
			}
			
			mSelf.dismiss();
		}

	};
	
}
