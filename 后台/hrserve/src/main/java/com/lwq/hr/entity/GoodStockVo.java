package com.lwq.hr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class GoodStockVo implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer gid;

    private String name ;

    private String label ;

    private Float price;

    private Integer stock;

    private String comment;


}
