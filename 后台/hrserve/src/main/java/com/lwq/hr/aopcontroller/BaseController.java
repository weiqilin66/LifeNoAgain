package com.lwq.hr.aopcontroller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.lwq.hr.utils.RequestBean;
import com.lwq.hr.utils.ResponseBean;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class BaseController {

	private Logger logger = Logger.getLogger(BaseController.class);
	public BaseController() {
		
	}
	
	protected RequestBean beforeAction(HttpServletRequest request, Class paramClass, RequestBean requestBean) throws Exception{
		// 将JSON格式的字符串构造成JSON对象
		String requestJsonStr = request.getParameter("message");
		logger.info("前台发送数据:  " + requestJsonStr);
		ObjectMapper objectMapper = new ObjectMapper();
		requestBean = objectMapper.readValue(requestJsonStr,RequestBean.class);
		List<Object> tempLists = requestBean.getParameterList();
		if(!"[]".equals(String.valueOf(tempLists))){
			List<Object> requestList = new ArrayList();
			for (int i = 0, n = tempLists.size(); i < n; i++) {
				requestList.add(mapToEntity(tempLists.get(i),paramClass));
			}
			requestBean.setParameterList(requestList);	
		}
		
		return requestBean;
	}

	public String excuteRequest(HttpServletRequest req,Class paramClass) {
		String responseJsonStr = "";
		RequestBean requestBean = new RequestBean();
		ResponseBean responseBean = new ResponseBean();
		try {
			HttpSession session = req.getSession();
			if (session.getAttribute("user") != null) {
//				User user = (User) session.getAttribute("user");
//				MDC.put("userid","OrganNo_" + user.getOrganNo()+ "_UserNo_" + user.getUserNo());
			}
			requestBean = beforeAction(req,paramClass,requestBean);
			responseBean = doAction(req,requestBean,responseBean);
			responseJsonStr = afterAction(responseBean);
		} catch (Exception e) {
			logger.error("IF执行出错 " + e.getMessage(), e);
//			responseJsonStr = "{\"retCode\":"+SunIFErrorMessage.UNKNOWN_ERROR+",\"retMsg\":"+SunIFErrorMessage.UNKNOWN_ERROR_MSG+"}";
		}
		return responseJsonStr;
	}

	protected String afterAction(ResponseBean responseBean) throws JsonProcessingException {
		return transObj2Json(responseBean);
		/*log.info("后台发送数据:  " + transObj2Json(responseBean));*/
	}

	protected ResponseBean doAction(HttpServletRequest req,RequestBean requestBean,ResponseBean responseBean) {
		return responseBean;
	}

/*	protected Object transJson2Obj(String jsonStr,String requestJsonStr) {
		Object object = null;
		try {
			object = objectMapper.readValue(requestJsonStr, paramClass);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return object;
	}*/

	protected String transObj2Json(Object object) throws JsonProcessingException {
		String retJson = "";
		ObjectMapper objectMapper = new ObjectMapper();
		retJson = objectMapper.writeValueAsString(object);
		return retJson;
	}

	protected Object mapToEntity(Object object,Class paramClass) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String str = objectMapper.writeValueAsString(object);
		Object paramBean = objectMapper.readValue(str, paramClass);
		return paramBean;
	}
}
