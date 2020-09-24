package com.lwq.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lwq.hr.entity.Shop;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LinWeiQi
 * @since 2020-05-22
 */
public interface ShopMapper extends BaseMapper<Shop> {

    @Select("select * from shop where enabled = 1")
    List<Shop> selectAllEnabled();
    // 折线图
    @Select("select * from shop")
    List<Shop> selectAll();

    @Select("select * from shop where enabled = 1")
    List<Shop> selBlackList();
}
