package com.example.demo.elasticsearch.search.service;


import com.example.demo.elasticsearch.search.pojo.EsInfo;

/**
 * @author Exrickx
 */
public interface SearchItemService {

	/**
	 * 同步索引
	 * @return
	 */
	int importAllItems();

	/**
	 * 获取ES基本信息
	 * @return
	 */
	EsInfo getEsInfo();
}
