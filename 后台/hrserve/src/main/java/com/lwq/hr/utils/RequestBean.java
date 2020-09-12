package com.lwq.hr.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * 请求bean
 * @author YZ
 * 2016年12月5日  下午4:33:53
 */
public class RequestBean  implements Serializable{
  
  private static final long serialVersionUID = 4372676277437983382L;
  
  private Map sysMap;
  private List<Object> parameterList;

  public Map getSysMap()
  {
    return this.sysMap;
  }

  public void setSysMap(Map sysMap) {
    this.sysMap = sysMap;
  }

  public List<Object> getParameterList() {
    return this.parameterList;
  }

  public void setParameterList(List<Object> parameterList) {
    this.parameterList = parameterList;
  }

  public static long getSerialversionuid() {
    return 4372676277437983382L;
  }
}