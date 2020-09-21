package com.lwq.hr.mapper;

import com.lwq.hr.entity.GoodLabel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LinWeiQi
 * @since 2020-09-21
 */
public interface GoodLabelMapper extends BaseMapper<GoodLabel> {

    @Select("select id ,label from good_label")
    List<GoodLabel> queryAll();
}
