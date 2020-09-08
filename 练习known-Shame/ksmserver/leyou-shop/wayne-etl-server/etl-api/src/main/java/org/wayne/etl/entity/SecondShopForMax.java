package org.wayne.etl.entity;

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
 * @since 2020-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SecondShopForMax implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer enabled;

    private String comment;


}
