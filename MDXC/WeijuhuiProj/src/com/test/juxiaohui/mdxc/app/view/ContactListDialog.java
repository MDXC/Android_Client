package com.test.juxiaohui.mdxc.app.view;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.app.ContactEditorActivity;
import com.test.juxiaohui.mdxc.app.FlightOrderActivity;
import com.test.juxiaohui.mdxc.app.FlightOrderActivity.IContactListener;
import com.test.juxiaohui.mdxc.data.ContactUser;
import com.test.juxiaohui.mdxc.manager.UserManager;

public class ContactListDialog extends Dialog 
{

	private Context mContext;
	private IContactListener mListener;
	
	private LinearLayout mMainView;
	private ListView mlistViewLayout;
	private ImageView mIvMaleSelected, mIvFemaleSelected;
	private LayoutInflater mInflater;
	private Button mBtnCancel;
	
	public static final int MALE = 0;
	public static final int FEMALE = 1;
	
	private static final int CANCEL = 2;

	
	private ContactListDialog mSelf;
	
	private int mCurrentType;
	private BaseContactsAdapter adapter;
	private String mOrderId ="";
	private FlightOrderActivity mFightActivity;
	public ContactListDialog(FlightOrderActivity flightActivity,IContactListener listener,String orderId) 
	{
		// TODO Auto-generated constructor stub
		super(flightActivity,R.style.cabin_class_dialog);
		mContext = flightActivity;
		mFightActivity = flightActivity;
		mListener = listener;	
		mSelf = this;
		mOrderId = orderId;
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
		mMainView = (LinearLayout) mInflater.inflate(R.layout.dialog_contact_list, null);
		mMainView.setLayoutParams(mainViewlp);
		mlistViewLayout = (ListView) mMainView.findViewById(R.id.dialog_contact_list);
		List<ContactUser> list = UserManager.getInstance().getContactUserList();
		list.add(0, ContactUser.NULL);
		adapter = new BaseContactsAdapter(mContext,list);
 		mlistViewLayout.setAdapter(adapter);
 		mBtnCancel = (Button) mMainView.findViewById(R.id.btn_cancle);
		mBtnCancel.setTag(CANCEL);
		mBtnCancel.setOnClickListener(mOnClickListener);
		
		setContentView(mMainView);
		
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

			case CANCEL:
				mSelf.dismiss();
				break;
			}
			
		}

	};
	
	class BaseContactsAdapter extends BaseAdapter{

		Context mContext;
		List<ContactUser> list;
		BaseContactsAdapter (Context context,List<ContactUser> contactUserList) {
			mContext = context;
			list = contactUserList;
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		    convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_contact_list_item,null);
		    RelativeLayout contactLayout = (RelativeLayout)convertView.findViewById(R.id.contact_layout);
		    RelativeLayout addContactLayout = (RelativeLayout)convertView.findViewById(R.id.add_contact_layout);
		    if (position == 0) {
		    	contactLayout.setVisibility(View.GONE);
		    	addContactLayout.setVisibility(View.VISIBLE);
		    	addContactLayout.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(mContext, ContactEditorActivity.class);
						intent.putExtra("order_id", mOrderId);
						mFightActivity.startActivityForResult(intent,FlightOrderActivity.REQ_CONTACT_INFO);
					}
				});
		    } else {
		    	contactLayout.setVisibility(View.VISIBLE);
		    	contactLayout.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ContactUser user = (ContactUser)list.get(position);
						mListener.onChangeContact(user);
					}
				});
		    	addContactLayout.setVisibility(View.GONE);
 			    TextView contactNameView = (TextView)convertView.findViewById(R.id.tv_contact_name_subtitle);
			    ContactUser contact = list.get(position);
			  
			    contactNameView.setText(contact.contactName);
			    
			    TextView contactNumberView = (TextView) convertView.findViewById(R.id.tv_contact_number_subtitle);
			    contactNumberView.setText(contact.contCountryCode+" "+contact.contPhone);
			    
			    TextView contactEmailView = (TextView)convertView.findViewById(R.id.tv_contact_email_subtitle);
			    contactEmailView.setText(contact.contEmail);
		    }
		    
		    return convertView;
		}
		
	}
	
	
}
