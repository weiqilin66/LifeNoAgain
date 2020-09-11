/*
package com.lwq.hr.aopservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.lwq.hr.dao.ExampleDao;
import com.lwq.hr.utils.BaseUtil;
import com.lwq.hr.utils.ImportUtil;
import com.lwq.hr.utils.RequestBean;
import com.lwq.hr.utils.ResponseBean;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



*/
/**
 * ... 接口实现类
 * 
 * @author:	...
 * @date:	2017年2月28日 上午10:49:50
 *//*

@Service("exampleService")
@Transactional
public class ExampleServiceImpl extends BaseService implements IExampleService {
	private Logger logger = Logger.getLogger(ExampleServiceImpl.class);
	// 数据库接口
	@Resource
	private ExampleDao exampleDao;
	
	*/
/**
	 * 执行接口逻辑
	 * 
	 * @author:	...
	 * @date:	2017年2月28日 上午10:53:30
	 *//*

	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	*/
/**
	 * 执行具体操作：增、删、改、查 等，操作成功后记录日志信息
	 * 
	 * @author:	...
	 * @date:	2017年2月28日 下午4:34:57
	 *//*

	@Override
	@SuppressWarnings({ "rawtypes" })
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) {
		try {
			// 获取参数集合
			Map sysMap = requestBean.getSysMap();
			// 获取操作标识
			String oper_type = (String) sysMap.get("oper_type");
			if (AOSConstants.OPERATE_QUERY.equals(oper_type)) {
				// 查询
				queryOperation(requestBean, responseBean);
			} else if (AOSConstants.OPERATE_ADD.equals(oper_type)) { 
				// 新增
				addOperation(requestBean, responseBean);
			} else if (AOSConstants.OPERATE_MODIFY.equals(oper_type)) { 
				// 修改
				modifyOperation(requestBean, responseBean);
			} else if(AOSConstants.OPERATE_DELETE.equals(oper_type)) { 
				// 删除
				deleteOperation(requestBean, responseBean);
			} else if (AOSConstants.OPERATE_IMPORT.equals(oper_type)) {
				// 导入
				importOperation(requestBean, responseBean);
			}
		} catch (Exception e) {
			logger.error("doAction异常",e);
		}
	}
	
	*/
/**
	 * 查询 ... 操作
	 * 
	 * @author:	...
	 * @date:	2017年2月28日 下午5:17:47
	 * 
	 * @throws Exception 
	 *//*

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		// condMap.put("cond1", "");
		// condMap.put("cond2", "");
		condMap = addExtraCondition(condMap);
		
		// 当前页数
		int currentPage = (int) sysMap.get("currentPage");
		// 每页数量
		int pageSize = 0;
		if (currentPage != -1) { // -1 表示查询全部数据
			pageSize = (int) sysMap.get("pageSize");
			if (pageSize <= 0) {
				pageSize = AOSConstants.PAGE_NUM;
			}
		}
		// 定义分页操作，pageSize = 0 时查询全部数据（不分页），currentPage <= 0 时查询第一页，currentPage > pages（超过总数时），查询最后一页
		Page page = PageHelper.startPage(currentPage, pageSize);
		// 查询分页记录
		List list = getList(exampleDao.select(condMap), page);
		// 获取总记录数
		long totalNum = page.getTotal();
		
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("currentPage", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalNum", totalNum);
		retMap.put("list", list);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
		// getList(exampleDao.select(condMap), page);
		// getString(exampleDao.select(condMap));
		// getInt(exampleDao.select(condMap));
		
	}
	
	*/
/**
	 * 新增 ... 操作
	 * 
	 * @author:	...
	 * @date:	2017年2月28日 下午5:17:49
	 * 
	 * @throws Exception
	 *//*

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void addOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		
		// 获取新增数据
		ExampleBean example = (ExampleBean) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		// condMap.put("cond1", "");
		// condMap.put("cond2", "");
		condMap.put("example", example);
		condMap = addExtraCondition(condMap);
		// 新增数据
		exampleDao.insert(condMap);
		
		// 记录操作日志
		String logContent = "";
		addLogInfo(logContent);
		
		// 拼装返回信息
		Map retMap = new HashMap();
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("新增成功");
		
		// logContent为日志内容，描述尽量详细
		// 如：logContent = "新增岗位信息，||{ 岗位号：0001，岗位名称：印章管理员，岗位类别：1-管理类 }"
	}

	*/
/**
	 * 修改 ... 操作
	 * 
	 * @author:	...
	 * @date:	2017年2月28日 下午5:17:52
	 * 
	 * @throws Exception 
	 *//*

	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	private void modifyOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		
		// 获取修改数据
		ExampleBean example = (ExampleBean) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		// condMap.put("cond1", "");
		// condMap.put("cond2", "");
		condMap.put("example", example);
		condMap = addExtraCondition(condMap);
		// 修改数据
		exampleDao.update(condMap);
		
		// 记录操作日志
		String logContent = "";
		addLogInfo(logContent);
		
		// 拼装返回信息
		Map retMap = new HashMap();
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功");
		
		// logContent为日志内容，描述尽量详细
		// 如：logContent = "修改岗位信息，||修改前：{ 岗位号：0001，岗位名称：印章管理员，岗位类别：1-管理类 }"
		// 					+ "，||修改后：{ 岗位号：0001，岗位名称：凭证管理员，岗位类别：1-管理类 }"
	}

	*/
/**
	 * 删除 ... 操作
	 * 
	 * @author:	...
	 * @date:	2017年2月28日 下午5:17:59
	 * 
	 * @throws Exception 
	 *//*

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private void deleteOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		// condMap.put("cond1", "");
		// condMap.put("cond2", "");
		condMap = addExtraCondition(condMap);
		// 删除数据
		exampleDao.delete(condMap);
		
		// 记录操作日志
		String logContent = "";
		addLogInfo(logContent);
		
		// 拼装返回信息
		Map retMap = new HashMap();
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功");
		
		// logContent为日志内容，描述尽量详细
		// 如：logContent = "删除岗位信息，||{ 岗位号：0001，岗位名称：印章管理员，岗位类别：1-管理类 }"
	}
	
	*/
/**
	 * 导入 ... 操作
	 * 
	 * @author:	...
	 * @date:	2017年3月27日 上午10:45:32
	 * 
	 * @throws Exception 
	 *//*

	@SuppressWarnings("unchecked")
	private void importOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取导入文件列表信息，包含每个文件在服务器的实际存储名称（带路径）
		List<Map<String, String>> fileInfoList = (List<Map<String, String>>)requestBean.getSysMap().get("uploadFileList");
		// 获取表头行数信息
		String headerRowNumStr = requestBean.getSysMap().get("headerRowNum") + "";
		if (BaseUtil.isBlank(headerRowNumStr)) {
			headerRowNumStr = "1";
		}
		// 遍历解析每个文件，执行导入操作
		for (Map<String, String> fileMap : fileInfoList) {
			String filePath = fileMap.get("saveFileName");
			// 获取导入数据（这种数据导入的文件一般都是临时存储，导入完成即可删除，故此处调用接口时传递参数 true ）
			List<HashMap<String, String>> dataList = ImportUtil.importExcel(filePath, Integer.parseInt(headerRowNumStr), true);
			if (dataList == null) {
				throw new Exception("导入文件格式有误！");
			}
			// 导入数据的实际处理，添加到数据库等
			for (HashMap<String, String> dataMap : dataList) {
				for (String key : dataMap.keySet()) {
					
					System.out.println(key + " | " + dataMap.get(key));
				}
			}
		}
	}
}
*/
