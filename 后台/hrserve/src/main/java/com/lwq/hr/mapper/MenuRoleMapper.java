package com.lwq.hr.mapper;

import com.lwq.hr.entity.MenuRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LinWeiQi
 * @since 2020-01-28
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    int insertBatchId(@Param("mids") int[] mids, int rid);
}
