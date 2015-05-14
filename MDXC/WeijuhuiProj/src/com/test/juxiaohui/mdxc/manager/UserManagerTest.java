package com.test.juxiaohui.mdxc.manager;

import android.test.AndroidTestCase;
import com.test.juxiaohui.common.data.User;
import com.test.juxiaohui.mdxc.data.ContactUser;
import com.test.juxiaohui.mdxc.data.Passenger;
import com.test.juxiaohui.mdxc.server.UserServer;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihao on 15/4/19.
 */
public class UserManagerTest extends AndroidTestCase {
    UserManager mUserManager;
    protected void setUp() throws Exception {
        super.setUp();
        mUserManager = UserManager.getInstance();
    }

    public void testSetContactUser()
    {
        mUserManager.logout();

        ContactUser contactUser;
        contactUser = mUserManager.getContactUser();
        Assert.assertEquals(contactUser, ContactUser.NULL);

        mUserManager.login("+86", "15510472558", "123456", "0", null);
        contactUser  = new ContactUser();
        contactUser.contactName = "Yi";
        //contactUser.mLastName = "Hao";
        contactUser.contEmail = "yhchinabest@163.com";
        contactUser.contPhone = "15510472558";
        mUserManager.setContactUser(contactUser);

        contactUser = mUserManager.getContactUser();
        Assert.assertEquals("Yi", contactUser.contactName);

        mUserManager.logout();
        mUserManager.login("+86", "15510472558", "123456","0", null);
        contactUser = mUserManager.getContactUser();
        Assert.assertEquals("Yi", contactUser.contactName);
    }

    public void testSetPassenger()
    {
        mUserManager.logout();
        Assert.assertNotNull(mUserManager.getPassengerList());

        mUserManager.login("+86", "15510472558", "123456", "0", null);
        List<Passenger> listPassenger = new ArrayList<Passenger>();
        Passenger passenger = new Passenger();
        passenger.mId = "1";
        passenger.mName = "a";
        listPassenger.add(passenger);
        passenger = new Passenger();
        passenger.mId = "2";
        passenger.mName = "b";
        listPassenger.add(passenger);

        mUserManager.setPassengerList(listPassenger);
        Assert.assertEquals(mUserManager.getPassengerById("1").mName, "a");

        mUserManager.logout();
        Assert.assertEquals(mUserManager.getPassengerList().size(), 0);

        mUserManager.login("+86", "15510472558", "123456","0", null);
        Assert.assertEquals(mUserManager.getPassengerList().size(), 2);
        Assert.assertEquals(mUserManager.getPassengerById("1").mName, "a");


    }

    public void testLogin()
    {
        //验证码登录
        //send code
        ((UserServer)mUserManager.mUserServer).sendCheckcode("+86", "18710161651");
        //get code
        String smsCode = ((UserServer)mUserManager.mUserServer).getSMSCode();
        //login
        String result = mUserManager.mUserServer.login("+86", "18710161651", "", "1", smsCode, null);
        Assert.assertEquals(result, "Success");

        Assert.assertEquals(mUserManager.login("+86", "15510472558", "123456", "0", null), UserManager.LOGIN_SUCCESS);

        Assert.assertEquals(mUserManager.login("+86", "15510472558", "123456", "0", null), UserManager.ALREADY_LOGIN);

        Assert.assertEquals(mUserManager.logout(), UserManager.LOGOUT_SUCCESS);

        Assert.assertEquals(mUserManager.login("+86", "15510472558", "1234567", "0", null), UserManager.INVALID_USERNAME_PASSWORD);


    }

    public void testGetUser()
    {
       Assert.assertTrue(User.NULL!=mUserManager.getUserInfo("+86", "15510472558"));
    }

}
