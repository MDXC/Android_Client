package com.test.juxiaohui.mdxc.mediator;

import java.util.ArrayList;

import com.test.juxiaohui.mdxc.data.ControlCode;

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
	
	public ArrayList<ControlCode> getNearbyPort();
	
	public ArrayList<ControlCode> getLastSearchControl();
	
	public ArrayList<ControlCode> getHostControl();
	
	/**
	 * 
	 * @param condition
	 * @return
	 */
	public ArrayList<ControlCode> getSearchResult(String condition);
	
}
