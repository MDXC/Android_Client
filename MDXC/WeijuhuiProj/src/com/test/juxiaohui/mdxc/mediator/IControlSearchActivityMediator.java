package com.test.juxiaohui.mdxc.mediator;

import java.util.ArrayList;

import com.test.juxiaohui.mdxc.data.CountryCode;

public interface IControlSearchActivityMediator 
{
	/**
	 * 添加搜索框
	 */
	public void addSearchView();
	/**
	 * 添加城市列表
	 */
	public void addControlList();
	/**
	 * 实时搜索
	 * @param condition
	 */
	public void realtimeSearch(String condition);
	
	/**
	 * 更新列表
	 */
	public void updateList();
	
	public ArrayList<CountryCode> getNearbyPort();
	
	public ArrayList<CountryCode> getLastSearchControl();
	
	public ArrayList<CountryCode> getHostControl();
	
	/**
	 * 
	 * @param condition
	 * @return
	 */
	public ArrayList<CountryCode> getSearchResult(String condition);
	
}
