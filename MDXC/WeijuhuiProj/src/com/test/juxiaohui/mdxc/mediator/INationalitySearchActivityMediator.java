package com.test.juxiaohui.mdxc.mediator;

import java.util.ArrayList;

import com.test.juxiaohui.mdxc.data.CityData;

public interface INationalitySearchActivityMediator 
{
	/**
	 * 添加搜索框
	 */
	public void addSearchView();
	/**
	 * 添加城市列表
	 */
	public void addNationalityList();
	/**
	 * 实时搜索
	 * @param condition
	 */
	public void realtimeSearch(String condition);
	
	/**
	 * 更新列表
	 */
	public void updateList();
	

	/**
	 * 
	 * @param condition
	 * @return
	 */
	public ArrayList<String> getSearchResult(String condition);
	
}