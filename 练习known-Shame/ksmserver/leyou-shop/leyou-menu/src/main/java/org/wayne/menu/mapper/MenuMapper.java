package org.wayne.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.wayne.menu.entity.Menu;
import org.wayne.menu.entity.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LinWeiQi
 * @since 2020-01-28
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getMenuById(int id);

    Menu t1(int id);

    List<Role> getAllMenuRoles(String url);

    List<Menu> getMenuWithRoles();
}
