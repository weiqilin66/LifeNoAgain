package com.lwq.hr.entity;

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
 * @since 2020-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GoodStock implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer gid;

    private Float price;

    private Integer stock;

    private String comment;


}
