package org.wayne.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.wayne.menu.entity.HrRole;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LinWeiQi
 * @since 2020-01-28
 */
public interface HrRoleMapper extends BaseMapper<HrRole> {

    int addRoles(int hrid, int[] rids);
}
