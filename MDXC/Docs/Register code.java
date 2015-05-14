/*
最好能参考sdk里的如下文档编写UI测试代码
docs/training/activity-testing/activity-ui-testing.html
docs/training/activity-testing/activity-unit-testing.html
docs/training/activity-testing/activity-functional-testing.html
*/

public interface IRegisterMediator
{
	void addUsernameView();
	void addPasswordView();
	void confirm();
	void cancel();
}

public class RegisterActivity extends Activity implements IRegisterMediator
{
	EditText mEdtUsername;
	EditText mEdtPassword;
	Button mBtnOK;
	Button mBtnCancel;
	public void onCreate(Bundle savedInstanceState)
	{
		setContentView(...);
		addUsernameView();
		addPasswordView();

	    //中间省略部分代码
	    mBtnOK.setOnClickListener(View view)
	    {
	    	confirm();
	    }

	    mBtnCancel.setOnClickListener(View view)
	    {
	    	cancel();
	    }

	}

	public void addUsernameView()
	{
		mEdtUsername = (EditText)findViewById(...);
	}

	public void addPasswordView();
	{
		mEdtPassword = (EditText)findViewById(...);
	}

	public void confirm()
	{
		//检查用户名和密码是否合理
		...
		//调用ServerManager进行注册
		ServerManager.getInstance().register(username, password);

		//startActivity()会封装Intent的相关逻辑
		LoginActivity.startActvity(this);
	}

	public void cancel()
	{
		finish();
	}

}