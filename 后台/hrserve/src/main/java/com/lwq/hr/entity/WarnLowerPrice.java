package com.lwq.hr.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2020-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WarnLowerPrice implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private int gid;

    private String shop;

    private String kw;

    private float price;


}
