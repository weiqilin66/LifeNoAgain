package com.lwq.hr.mapper;

import com.lwq.hr.entity.ShopWarning;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LinWeiQi
 * @since 2020-09-24
 */
public interface ShopWarningMapper extends BaseMapper<ShopWarning> {

    List<HashMap> queryAll();
}
