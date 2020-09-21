package com.lwq.hr.mapper;

import com.lwq.hr.entity.GoodMain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LinWeiQi
 * @since 2020-09-15
 */
public interface GoodMainMapper extends BaseMapper<GoodMain> {

    @Select("select * from good_main")
    List<GoodMain> queryAll();
}
