package com.lwq.hr.aopcontroller;

import org.springframework.stereotype.Controller;


/**
 * ... 控制器
 * 
 * @author:	...
 * @date:	2017年2月28日 下午7:34:37
 */
@Controller
public class ExampleController extends BaseController {
	/*private Logger log = Logger.getLogger(getClass());
	// 服务接口
	@Resource
	private IExampleService exampleService;

	*//**
	 * 执行操作请求
	 *
	 * @author:	...
	 * @date:	2017年2月28日 下午7:40:39
	 *//*
	@RequestMapping(value = "/example.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="增删改查", operationName="操作...信息")
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 执行请求，附带请求参数Bean类型
		return super.excuteRequest(request, ExampleBean.class);
	}

	*//**
	 * 调用服务接口，执行具体操作
	 *
	 * @author:	...
	 * @date:	2017年2月28日 下午7:44:10
	 *//*
	@Override
	protected ResponseBean doAction(HttpServletRequest request, RequestBean requestBean, ResponseBean responseBean) {
		try {
			responseBean = exampleService.execute(requestBean);
		} catch (Exception e) {
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			log.error("调用接口方法出错： " + e.getMessage(), e);
		}
		return responseBean;
	}*/
}
