package com.lwq.hr.utils;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @author: LinWeiQi
 */
@Data
public class RespPageBean {
    private long total;
    private List<?> data;
}
