package com.test.juxiaohui.mdxc.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yihao on 15/4/18.
 * 用来表示联系人,既可以给机票使用，也可以给酒店使用
 */
public class ContactUser implements Comparable,Serializable{
    //public String mFirstName = "";
    //public String mLastName = "";
    //public String mEmail = "";
    //public String mPhoneNumber = "";
	public String contactIndex = "0";
    public String contactName = "";
    public String contCountryCode = "";
    public String contPhone = "";
    public String contEmail = "";
    public String recipient = "";
    public String reciPhone = "";
    public String reciAddress = "";
    public String reciPostalCode = "";
    public String pickUpTime = "";

    public static ContactUser NULL = new ContactUser();

    public static JSONObject toJSON(ContactUser contactUser)
    {
        if(null!=contactUser)
        {
            JSONObject obj = new JSONObject();
            try {
//                obj.put("firstName", contactUser.mFirstName);
//                obj.put("lastName", contactUser.mLastName);
            	obj.put("contactIndex", contactUser.contactIndex);
                obj.put("contactName", contactUser.contactName);
                obj.put("contEmail", contactUser.contEmail);
                obj.put("contPhone", contactUser.contPhone);
                obj.put("contCountryCode", contactUser.contCountryCode);
                return obj;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static ContactUser fromJSON(JSONObject jsonObject)
    {
        if(null!=jsonObject)
        {
            ContactUser contactUser = new ContactUser();
            try {
                contactUser.contactName = jsonObject.getString("contactName");
                contactUser.contEmail = jsonObject.getString("contEmail");
                contactUser.contPhone = jsonObject.getString("contPhone");
                contactUser.contCountryCode = jsonObject.getString("contCountryCode");
                contactUser.contactIndex = jsonObject.getString("contactIndex");
                return contactUser;
            } catch (JSONException e) {
                e.printStackTrace();
                return ContactUser.NULL;
            }
        }
        return ContactUser.NULL;
    }

    public static ContactUser createTestUser(){
        ContactUser contactUser = new ContactUser();
        contactUser.contactName = "yihao";
        contactUser.contCountryCode = "+86";
        contactUser.contPhone = "15510472558";
        contactUser.contEmail = "yhchinabest@163.com";
        contactUser.contactIndex = "0";
        return contactUser;
    }

	@Override
	public int compareTo(Object another) {
        ContactUser tmp = (ContactUser)another;
		return contactIndex.compareTo(tmp.contactIndex);
	}
}
