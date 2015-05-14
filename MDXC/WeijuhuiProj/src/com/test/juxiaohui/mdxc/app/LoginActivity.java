package com.test.juxiaohui.mdxc.app;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.R;
import com.test.juxiaohui.common.data.User;
import com.test.juxiaohui.mdxc.data.CountryCode;
import com.test.juxiaohui.mdxc.manager.UserManager;
import com.test.juxiaohui.mdxc.mediator.ILoginMediator;

/**
 * Created by yihao on 15/3/4.
 * 当前只留下手动登录方式。
 */
public class LoginActivity extends Activity implements ILoginMediator{

    EditText mEtxUsername;
    EditText mEtxPassword;
    Button mBtnOK;
    Button mBtnCancel;
    Button mBtnRegister;
    String mLoginResult = "";
    RelativeLayout mLayoutSplash;
    LinearLayout mLayoutContent;
    Spinner mElvCountryCode;
    CountryCode mSelectCountryCode = CountryCode.NULL;
    int selectIndex = 0;
    MySpinnerAdapter mSpinnerAdapter = null;
    Button mBtnSendCheckCode = null;
    EditText mEtxCheckCode = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mdxc_activity_login);
        mLayoutSplash = (RelativeLayout) findViewById(R.id.rl_splash);
        mLayoutContent = (LinearLayout) findViewById(R.id.ll_content);

        addUsernameView();
        addPasswordView();
        addCountryCodeView();
        addCheckCodeView();

        mBtnOK = (Button)findViewById(R.id.button_OK);
        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });

        mBtnCancel = (Button)findViewById(R.id.button_Cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        //当前未登录
        //showProgress();
//        if(!UserManager.getInstance().isLogin())
//        {
            loadLoginInfo();
//            String type = checkLoginParam(mSelectCountryCode.mCode, mEtxUsername.getText().toString(), mEtxPassword.getText().toString(), "");
//            if(type.equals("0")){
//
//                Thread t= new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //loginFromCache();
//                        if(UserManager.getInstance().isLogin())
//                        {
//                            gotoNext();
//                        }
//                        else
//                        {
//                            hideProgress();
//                        }
//
//                    }
//                });
//                t.start();
//            }
//            else
//            {
//                //手动登录
//                //hideProgress();
//            }

//        }
//        else{
//            gotoNext();
//        }
        
    }

    @Override
    public void addUsernameView() {
        mEtxUsername  = (EditText)findViewById(R.id.editText_username);
    }

    @Override
    public void addPasswordView() {
        mEtxPassword = (EditText)findViewById(R.id.editText_password);
    }

    /**
     * 当用户登录过一次后，会缓存用户名和密码
     * 下次就会自动从缓存中取出用户名和密码进行登录
     */
    @Override
    public void loginFromCache() {
        if(!UserManager.getInstance().isLogin())
        {
            String countryCode = UserManager.getInstance().getCachedCountryCode();
            String username = UserManager.getInstance().getCachedUsername();
            String password = UserManager.getInstance().getCachedPassword();
            if(null!=username && null!=password)
            {
                mLoginResult = UserManager.getInstance().login(countryCode, username, password, "0", null);
                if(!mLoginResult.equals(UserManager.LOGIN_SUCCESS))
                {
                    showErrorMessage(mLoginResult);
                }
            }
        }
    }

    @Override
    public void confirm() {
        boolean isValid = true;
        final String countryCode = mSelectCountryCode.mCode;
        final String username = mEtxUsername.getEditableText().toString();
        final String password = mEtxPassword.getEditableText().toString();
        final String checkCode = mEtxCheckCode.getEditableText().toString();
        final String type = checkLoginParam(countryCode, username, password, checkCode);

        if(!type.equals("-1"))
        {
            showErrorMessage("Waiting for login...");

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    if(type.equals("0")){
                        mLoginResult = UserManager.getInstance().login(mSelectCountryCode.mCode, username, password, "0", null);
                    }
                    else if(type.equals("1")){
                        mLoginResult = UserManager.getInstance().login(mSelectCountryCode.mCode, username, null, "1", checkCode);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mLoginResult.equals(UserManager.LOGIN_SUCCESS))
                            {
                                showErrorMessage("Login Success");
                                finish();
                            }
                            else
                            {
                                showErrorMessage(mLoginResult);
                            }
                        }
                    });
                }
            });
            t.start();

        }

    }

    @Override
    public void cancel() {
        finish();
    }

    @Override
    public void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLayoutSplash.setVisibility(View.INVISIBLE);
                mLayoutContent.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLayoutSplash.setVisibility(View.INVISIBLE);
                mLayoutContent.setVisibility(View.VISIBLE);
            }
        });

    }

    /**
     * 添加国家代码选择
     */
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
	               CountryCodeActivity.startActivity(LoginActivity.this);
	               mSpinnerAdapter.mCountryCodeList.remove(mSpinnerAdapter.mCountryCodeList.size() -2);
	            }
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
 			}
        	
        });
    }

    /**
     * 如果用户没有注册或者忘记密码，可以通过发送短信验证码来登录
     */
    @Override
    public void addCheckCodeView() {
        mBtnSendCheckCode = (Button)findViewById(R.id.button_send_checkCode);
        mBtnSendCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance().sendCheckcode(mSelectCountryCode.mCode, mEtxUsername.getText().toString());
            }
        });
        mEtxCheckCode = (EditText)findViewById(R.id.editText_checkCode);
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


    /**
     * 读取存储信息
     */
    @Override
    public void loadLoginInfo() {
        String countryCode = UserManager.getInstance().getCachedCountryCode();
        List<CountryCode> countryCodeList = CountryCode.getDefaultCodes();
        for(CountryCode code:countryCodeList)
        {
            if(code.mCode.equals(countryCode)){
                mSelectCountryCode = code;
                mElvCountryCode.setSelection(countryCodeList.indexOf(code));
            }

            break;
        }

        String username = UserManager.getInstance().getCachedUsername();
        mEtxUsername.setText(username);

        String password = UserManager.getInstance().getCachedPassword();
        mEtxPassword.setText(password);
    }

    @Override
    public String checkLoginParam(String coutryCode, String phoneNumber, String password, String checkCode) {
        if(coutryCode==null||coutryCode.length()==0)
        {
            showErrorMessage("Coutry Code is empty!");
            return "-1";
        }

        if(phoneNumber==null||phoneNumber.length()==0)
        {
            showErrorMessage("Phone Number is empty!");
            return "-1";
        }

        if(password!=null&&password.length()>0){
            return "0";
        }

        if(checkCode!=null&&checkCode.length()>0){
            return "1";
        }
        showErrorMessage("Password or Checkcode is empty!");
        return "-1";
    }

    public void showErrorMessage(final String message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, message,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getLoginResult()
    {
        Log.v(DemoApplication.TAG, "Login Result = " + mLoginResult);
        return mLoginResult;
    }


    public void onClickRegister(View view)
    {
    	Intent intent = new Intent(this, RegisterActivity.class);
    	startActivity(intent);
    }


    private void gotoNext(){
        //EntryActivity.startActivity(LoginActivity.this);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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