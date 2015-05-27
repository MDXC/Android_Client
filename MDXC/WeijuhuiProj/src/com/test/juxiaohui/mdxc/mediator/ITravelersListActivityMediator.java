package com.test.juxiaohui.mdxc.mediator;

import java.util.List;

import com.test.juxiaohui.mdxc.data.Passenger;

public interface ITravelersListActivityMediator {
   public void addTravelerListView();
   public void addNoDataView();
   public void addInfoMationer();
   
   
   public List<Passenger> getTravelersList();
}
