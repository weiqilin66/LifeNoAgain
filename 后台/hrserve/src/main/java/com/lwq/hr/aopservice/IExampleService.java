package com.lwq.hr.aopservice;


import com.lwq.hr.utils.RequestBean;
import com.lwq.hr.utils.ResponseBean;

/**
 * ... 接口
 * 
 * @author:	...
 * @date:	2017年2月28日 上午10:45:04
 */
public interface IExampleService {
	
	ResponseBean execute(RequestBean requestBean) throws Exception;
	
}
