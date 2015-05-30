package com.test.juxiaohui.mdxc.app;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.data.ContactUser;
import com.test.juxiaohui.mdxc.data.CountryCode;
import com.test.juxiaohui.mdxc.data.FlightOrder;
import com.test.juxiaohui.mdxc.manager.FlightOrderManager;
import com.test.juxiaohui.mdxc.manager.UserManager;
import com.test.juxiaohui.mdxc.manager.UtilManager;
import com.test.juxiaohui.mdxc.mediator.IContactEditMediator;

/**
 * Created by yihao on 15/5/21.
 */
public class ContactEditorActivity extends Activity implements IContactEditMediator {
	
	EditText firstNameEdit;
	EditText lastNameEdit;
	EditText emailEdit;
	EditText phoneNumberEdit;
	RelativeLayout mComfimContactInfoLayout;  
	Spinner mElvCountryCode;
	CountryCode mSelectCountryCode = CountryCode.NULL;
	int selectIndex = 0;
	MySpinnerAdapter mSpinnerAdapter = null;
	ContactUser contactUser = ContactUser.NULL;
	private String mOrderId = "";
	
	private FlightOrder mFlightOrder = null;
	String contactIndex = null;
	private boolean isClickDone = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderId = getIntent().getStringExtra("order_id");
        mFlightOrder = FlightOrderManager.getInstance().getFlightOrderbyId(mOrderId);
        initView();
        String contactIndex = getIntent().getExtras().getString("contactIndex");
        initData(contactIndex);
    }
    
    private void initView(){
    	  setContentView(R.layout.activity_contact_editor);
          addFirstNameView();
          addLastNameView();
          addEmailView();
          addCountryCodeView();
          addPhoneNumberView();
    }

    @Override
    public void addFirstNameView() {
    	firstNameEdit = (EditText)findViewById(R.id.editText_contact_firstname);
    }

    @Override
    public void addLastNameView() {
    	lastNameEdit = (EditText)findViewById(R.id.editText_contact_lastname);
    }

    @Override
    public void addEmailView() {
    	emailEdit = (EditText)findViewById(R.id.editText_contact_email);
    }

    @Override
    public void addCountryCodeView() {
    	final List<String> countryCodeList = CountryCode.convertCodeListToString(CountryCode.getDefaultCodes());
        countryCodeList.add("More");
        mSelectCountryCode = CountryCode.getDefaultCodes().get(0);
        mElvCountryCode = (Spinner)findViewById(R.id.expandableListView_countryCode);
        mSpinnerAdapter = new MySpinnerAdapter(countryCodeList); 
        mElvCountryCode.setAdapter(mSpinnerAdapter);
        

        mElvCountryCode.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
			    if(arg2<CountryCode.getDefaultCodes().size())
	            {
	               mSelectCountryCode = CountryCode.getDefaultCodes().get(arg2);
	            }
	             else
	            {
	               CountryCodeActivity.startActivity(ContactEditorActivity.this);
	               mSpinnerAdapter.mCountryCodeList.remove(mSpinnerAdapter.mCountryCodeList.size() -2);
	            }
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
 			}
        	
        });
    }
    
    
    
class MySpinnerAdapter implements SpinnerAdapter {
		
    	List<String> mCountryCodeList;
    	
    	public MySpinnerAdapter(List<String> countryCodeList){
    		mCountryCodeList = countryCodeList;
    	}
    	
    	
		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
          View view = getLayoutInflater().inflate(R.layout.item_country_code, null);
          TextView tv = (TextView) view.findViewById(R.id.textView_country_code);
          tv.setText(mCountryCodeList.get(position));
          return view;
		}
		
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mCountryCodeList.get(position);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mCountryCodeList.size();
		}
		
		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			 View view = getLayoutInflater().inflate(R.layout.item_country_code, null);
             TextView tv = (TextView) view.findViewById(R.id.textView_country_code);
             tv.setText(mCountryCodeList.get(position));
             tv.setTextColor(Color.WHITE);
             return view;
		}
	}

    @Override
    public void addPhoneNumberView() {
      phoneNumberEdit = (EditText)findViewById(R.id.editText_contact_phonenumber);
    }

    @Override
    public void confirm() {
    	
//    	  ContactUser mContactUser = null;
//    	  if (contactIndex != null) {
//    		
//    		 mContactUser = UserManager.getInstance().getContactUserById(contactIndex);
//    		
//    	  } else {
//    		
//    		 mContactUser = new ContactUser(); 
//    	  }
    	  String fristName = firstNameEdit.getText().toString().trim();
    	  String lastName = lastNameEdit.getText().toString().trim();
    	  if (!"".equals(fristName) && !"".equals(lastName)) {
    		  
    		 contactUser.contactName = fristName + "/" +lastName;
    	  
    	  } else if (!"".equals(fristName) && "".equals(lastName)) {
    		  
    		 contactUser.contactName = fristName;
    		  
          } else if ("".equals(fristName) && !"".equals(lastName)) {
    		
        	 contactUser.contactName = lastName;
    	  
    	  }
    	 
    	  contactUser.contEmail = emailEdit.getText().toString();
    	  contactUser.contPhone = phoneNumberEdit.getText().toString();
    	  contactUser.contCountryCode =  mSelectCountryCode.mCode;
    	  if(mFlightOrder != FlightOrder.NULL){
        	  mFlightOrder.mContactUser  = contactUser;
    	  }

          UserManager.getInstance().setContactUser(contactUser);
          Intent intent = new Intent();
          intent.putExtra("contactIndex", UserManager.getInstance().getContactUser().contactIndex);
          setResult(0, intent);
          isClickDone = true;
          finish();
    }

    @Override
    public void cancel() {

    }
    
    @Override
    public void finish() {
    	// TODO Auto-generated method stub
    	if(!isClickDone){
    		Intent intent = new Intent();
            intent.putExtra("contactIndex", "");
            setResult(0, intent);
    	}
    	super.finish();
    	  
          
    }
    
    private void initData(String contactIndex){
    	
    	if (contactIndex != null) {
    		contactUser = UserManager.getInstance().getContactUserById(contactIndex);
    	}
		
    	if (ContactUser.NULL != contactUser) {
    		
    		String[] contactName = contactUser.contactName.split("/");
    		firstNameEdit.setText(contactName[0]);
    		lastNameEdit.setText(contactName[1]);
    		emailEdit.setText(contactUser.contEmail);
    		phoneNumberEdit.setText(contactUser.contPhone);
    		int index = mSpinnerAdapter.mCountryCodeList.indexOf(contactUser.contCountryCode);
    		mElvCountryCode.setSelection(index);
    		
    	}
    	
    	mComfimContactInfoLayout = (RelativeLayout)findViewById(R.id.confirm_contact_info);
    	mComfimContactInfoLayout.setOnClickListener(new RelativeLayout.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (("".equals(firstNameEdit.getText().toString().trim()))) {
					
					Toast.makeText(getApplicationContext(), "Please Input First Name", Toast.LENGTH_SHORT).show();
					
				} else if("".equals(lastNameEdit.getText().toString().trim())) { 
					
					Toast.makeText(getApplicationContext(), "Please Input Last Name", Toast.LENGTH_SHORT).show();

				} else if("".equals(emailEdit.getText().toString().trim())) {
					
					Toast.makeText(getApplicationContext(), "Please Input Your Email", Toast.LENGTH_SHORT).show();
					
				} else if("".equals(phoneNumberEdit.getText().toString().trim())) {
					
					Toast.makeText(getApplicationContext(), "Please Input Your Phone Number", Toast.LENGTH_SHORT).show();

				} else {
					
					confirm();
					
				}
				
			}
		});
    }
    
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	 if (resultCode == RESULT_OK) {
             CountryCode control = (CountryCode)data.getSerializableExtra("control");
             mSelectCountryCode = control;
             mSpinnerAdapter.mCountryCodeList.add(0, control.mCode);
             mElvCountryCode.setAdapter(mSpinnerAdapter);
          }
         mElvCountryCode.invalidate();
         super.onActivityResult(requestCode, resultCode, data);
    }
}