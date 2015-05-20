package com.test.juxiaohui.mdxc.app;

import android.app.Activity;
import android.os.Bundle;
import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.mediator.IContactEditMediator;

/**
 * Created by yihao on 15/5/21.
 */
public class ContactEditorActivity extends Activity implements IContactEditMediator {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_editor);
    }

    @Override
    public void addFirstNameView() {

    }

    @Override
    public void addLastNameView() {

    }

    @Override
    public void addEmailView() {

    }

    @Override
    public void addCountryCodeView() {

    }

    @Override
    public void addPhoneNumberView() {

    }

    @Override
    public void confirm() {

    }

    @Override
    public void cancel() {

    }
}