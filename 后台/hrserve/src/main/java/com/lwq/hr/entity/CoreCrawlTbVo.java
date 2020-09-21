package com.lwq.hr.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Description:
 * @author: LinWeiQi
 */
@Data
public class CoreCrawlTbVo {

    private int id;
    private String label ;
    private String name ;
    private int stock ;
    private int advance ;
    private Timestamp lastUpdate;
    private int totalSales ;
    private boolean enabled;

}
