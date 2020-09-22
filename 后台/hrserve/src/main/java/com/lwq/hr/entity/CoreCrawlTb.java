package com.lwq.hr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author LinWeiQi
 * @since 2020-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CoreCrawlTb implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer gid;

    /**
     * 0-3优先级
     */
    private String advance;

    private String lastUpdate;

    private Integer totalSales;

    private boolean enabled;

    private boolean finished;


}
