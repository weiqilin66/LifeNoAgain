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
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GoodKeyWord implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer gid;

    private String base;

    private String include1;

    private String include2;
    private String include3;

    private String enclude1;

    private String enclude2;
    private String enclude3;


}
