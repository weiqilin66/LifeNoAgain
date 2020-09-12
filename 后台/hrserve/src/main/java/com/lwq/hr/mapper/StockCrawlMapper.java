package com.lwq.hr.mapper;

import java.util.HashMap;
import java.util.List;

/**
 * ...	数据库操作接口
 * 
 * @author:	
 * @date:	2017年12月7日 下午5:11:45
 */
public interface StockCrawlMapper {

	/**
	 * 新增 ... 信息
	 */
	public int insert(HashMap<String, Object> map);
	
	/**
	 * 删除 ... 信息
	 */
	public int delete(HashMap<String, Object> map);
	
	/**
	 * 修改 ... 信息
	 */
	public int update(HashMap<String, Object> map);
	
	/**
	 * 查询 ... 信息
	 */
	public List<HashMap<String, Object>> select(HashMap<String, Object> map);

}
