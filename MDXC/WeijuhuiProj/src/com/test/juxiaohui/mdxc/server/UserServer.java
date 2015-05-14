package com.test.juxiaohui.mdxc.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;
import com.test.juxiaohui.mdxc.manager.UserManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import com.test.juxiaohui.common.dal.IUserServer;
import com.test.juxiaohui.common.data.User;
import com.test.juxiaohui.utils.SyncHTTPCaller;

public class UserServer implements IUserServer {

	
	@Override
	public String register(String countryCode, String username, String password, String checkcode) {
		// TODO Auto-generated method stub
		String url = "http://64.251.7.148/user/app/register/index.json";

//		JSONObject json = new JSONObject();
//		try {
//			json.put("userName", countryCode+username);
//			json.put("password", password);
//			json.put("confirmPassword", password);
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		List params = new ArrayList();
		params.add(new BasicNameValuePair("userName", countryCode+username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("confirmPassword", password));
		params.add(new BasicNameValuePair("checkCode", checkcode));
		SyncHTTPCaller<String> caller;
		caller = new SyncHTTPCaller<String>(url, "", params) {

			@Override
			public String postExcute(String result) {
				String resultObj = null;
				try {
					JSONObject json = new JSONObject(result);
					resultObj = json.getString("status");
					if(resultObj.equals("200"))
					{
						return "Success";
					}
					else
					{
						return "Fail";
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return "Fail";
				}

			}
		};
		return caller.execute();
	}

	/**
	 * 登录
	 * @param username
	 * @param password
	 * @param type
	 * @param user 用来将登录后的用户信息返回，主要是为了返回一个id
	 * @return
	 */
	@Override
	public String login(String countryCode, String username, String password, String type, String smsCode, User user) {
		String url = "http://64.251.7.148/user/app/login/index.json";
		List params=new ArrayList();
		params.add(new BasicNameValuePair("userName", countryCode + username));
		params.add(new BasicNameValuePair("type",type));
		if(type.equals("0")){
			params.add(new BasicNameValuePair("password",password));
		}
		else if(type.equals("1")){
			params.add(new BasicNameValuePair("checkCode",smsCode));

		}
		SyncHTTPCaller<String> caller = new SyncHTTPCaller<String>(
				url, null, params, SyncHTTPCaller.TYPE_POST, "") {

			@Override
			public String postExcute(String result) {
				String resultObj = "Fail";
				try {
					JSONObject json = new JSONObject(result);
					resultObj = json.getString("status");
					if(resultObj.equals("200"))
					{
						//＃
						//user.setId(id);
						return "Success";
					}
					else
					{
						return "Fail";
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return "Fail";
				}

			}
		};
		return caller.execute();
	}

	@Override
	public User getCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String logout() {
		return "Success";

		/*String url = "http://64.251.7.148:8081/user-web/app/login/logout";
		SyncHTTPCaller<String> caller = new SyncHTTPCaller<String>(
				url, null, "") {

			@Override
			public String postExcute(String result) {
				String resultObj = "Failed";
				try {
					JSONObject json = new JSONObject(result);
					resultObj = json.getString("status");
					if(resultObj.contains("200")){
						resultObj = "Success";
					}
					else{
						resultObj = "Failed";
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return resultObj;
			}
		};
		return caller.execute();*/
	}

	@Override
	public void sendCheckcode(String countryCode, String phoneNumber) {
		String url = "http://64.251.7.148/user/app/register/checkcode.json";
		List params=new ArrayList();
		params.add(new BasicNameValuePair("userName", countryCode + phoneNumber));
		SyncHTTPCaller<String> caller = new SyncHTTPCaller<String>(
				url, null, params) {

			@Override
			public String postExcute(String result) {
				String resultObj = null;
				try {
					JSONObject json = new JSONObject(result);
					resultObj = json.getString("status");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return resultObj;
			}
		};
		caller.execute();
	}

	@Override
	public User getUserInfo(String countryCode, final String username) {
		String url = "http://64.251.7.148/user/app/user/index.json";
		List params=new ArrayList();
		params.add(new BasicNameValuePair("user.mobilePhone", countryCode+username));
		SyncHTTPCaller<User> caller = new SyncHTTPCaller<User>(
				url, null, params) {
			@Override
			public User postExcute(String result) {
				User resultObj = new User();
				try {
					JSONObject json = new JSONObject(result);
					json = json.getJSONObject("user");
					resultObj.setId(json.getString("id"));
					resultObj.setUsername(username);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return resultObj;
			}
		};
		return caller.execute();
	}

	public String getSMSCode(){
		HttpPost post = new HttpPost("http://64.251.7.148/user/app/register/pullcheckcode.json");
		HttpResponse httpResponse;
		try {
			httpResponse = new DefaultHttpClient().execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String str = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
				try {
					JSONObject json = new JSONObject(str);
					Iterator<String> keys = json.keys();
					while(keys.hasNext())
					{
						final String number = keys.next();
						Log.v("SMS", number);
						final String checkCode = json.getString(number);
							return checkCode;
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}
