package com.lwq.hr.utils;

import java.io.Serializable;
import java.util.Map;
/**
 * 返回Bean
 * @author YZ
 * 2016年12月2日  上午9:13:13
 */
public class ResponseBean  implements Serializable{
  private static final long serialVersionUID = 3532176852796649340L;
  
  private String retCode;
  private String retMsg;
  private Map<Object, Object> retMap;

  public String getRetCode()
  {
    return this.retCode;
  }

  public void setRetCode(String retCode) {
    this.retCode = retCode;
  }

  public String getRetMsg() {
    return this.retMsg;
  }

  public void setRetMsg(String retMsg) {
    this.retMsg = retMsg;
  }

  public Map<Object, Object> getRetMap() {
    return this.retMap;
  }

  public void setRetMap(Map<Object, Object> retMap) {
    this.retMap = retMap;
  }

  public static long getSerialversionuid() {
    return 3532176852796649340L;
  }
}