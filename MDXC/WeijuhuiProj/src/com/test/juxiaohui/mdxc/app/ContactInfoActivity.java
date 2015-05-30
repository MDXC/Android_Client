package com.test.juxiaohui.mdxc.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.data.ContactUser;
import com.test.juxiaohui.mdxc.manager.UserManager;
import com.test.juxiaohui.mdxc.mediator.IContactInfoActivityMediator;

public class ContactInfoActivity extends Activity implements IContactInfoActivityMediator,OnClickListener,OnItemClickListener{

	List<ContactUser> contactUserList = new ArrayList<ContactUser>();
	LinearLayout noDataView = null;
	ListView contactListView = null;
	Button contactUsBtn = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initContentView();
	}
	
	
	private void initContentView(){
		setContentView(R.layout.activity_contacts_list);
		addContactListView();
		addContactNoDataView();
		addContacttoUsView();
		
		if (contactUserList.size() == 0) {
			noDataView.setVisibility(View.VISIBLE);
			contactListView.setVisibility(View.GONE);
		} else {
			noDataView.setVisibility(View.GONE);
			contactListView.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int location, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,ContactEditorActivity.class);
		intent.putExtra("contactIndex", contactUserList.get(location).contactIndex);
		startActivityForResult(intent,1);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addContactListView() {
		// TODO Auto-generated method stub
		contactListView = (ListView)findViewById(R.id.contact_list);
		BaseContactsAdapter adapter = new BaseContactsAdapter(this);
		contactListView.setAdapter(adapter);
		contactListView.setOnItemClickListener(this);
	}

	@Override
	public void addContactNoDataView() {
		// TODO Auto-generated method stub
		 noDataView = (LinearLayout)findViewById(R.id.no_contact_list_view);
	}

	@Override
	public void addContacttoUsView() {
		// TODO Auto-generated method stub
		contactUsBtn = (Button)findViewById(R.id.contact_us_btn);
		contactUsBtn.setOnClickListener(this);
	}

	@Override
	public List<ContactUser> getContactList() {
		// TODO Auto-generated method stub
		contactUserList = UserManager.getInstance().getContactUserList();
		return contactUserList;
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		addContactListView();
	}
     
	
	class BaseContactsAdapter extends BaseAdapter{

		Context mContext;
		List<ContactUser> list;
		BaseContactsAdapter(Context context){
			mContext = context;
			list = getContactList();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contacts,null);
		    
		    TextView contactNameView = (TextView)convertView.findViewById(R.id.contact_name);
		    ContactUser contact = list.get(position);
		  
		    contactNameView.setText(contact.contactName);
		    
		    TextView contactNumberView = (TextView) convertView.findViewById(R.id.contact_number);
		    contactNumberView.setText(contact.contCountryCode+" "+contact.contPhone);
		    
		    TextView contactEmailView = (TextView)convertView.findViewById(R.id.contact_email);
		    contactEmailView.setText(contact.contEmail);
		    return convertView;
		}
		
	}
}
