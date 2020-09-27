package com.lwq.hr.mapper;

import com.lwq.hr.entity.GoodStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lwq.hr.entity.GoodStockVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LinWeiQi
 * @since 2020-09-18
 */
public interface GoodStockMapper extends BaseMapper<GoodStock> {

    @Select("select * from good_main where name like '%'||#{title}||'%'")
    List<String> chooseGoodTitle(String title);

    List<HashMap> queryAll();

    List<GoodStock> checkByStockId(@Param("stock") GoodStockVo stock);

    List<HashMap> queryAllWithKeyWord();

    @Update("update good_stock set comment =#{comment} where gid = #{gid}")
    int updateCommentById(@Param("gid") int gid, @Param("comment") String comment);

    HashMap queryByGid(int gid);
}
