package com.lwq.hr.mapper;

import com.lwq.hr.entity.MyFocus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LinWeiQi
 * @since 2020-06-23
 */
public interface MyFocusMapper extends BaseMapper<MyFocus> {

    @Select("select * from my_focus order by enabled desc")
    List<MyFocus> queryAll();
}
