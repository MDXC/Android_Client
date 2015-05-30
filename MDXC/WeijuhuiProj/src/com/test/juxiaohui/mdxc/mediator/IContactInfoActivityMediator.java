package com.test.juxiaohui.mdxc.mediator;

import java.util.List;

import com.test.juxiaohui.mdxc.data.ContactUser;

public interface IContactInfoActivityMediator {
      public void addContactListView();
      public void addContactNoDataView();
      public void addContacttoUsView();
      
      
      public List<ContactUser> getContactList();
}
