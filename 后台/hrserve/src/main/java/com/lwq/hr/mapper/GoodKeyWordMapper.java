package com.lwq.hr.mapper;

import com.lwq.hr.entity.GoodKeyWord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lwq.hr.entity.GoodMain;
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
 * @since 2020-09-15
 */
public interface GoodKeyWordMapper extends BaseMapper<GoodKeyWord> {

    @Select("select t1.id as id , concat(label,`name`) as name " +
            "from good_key_word t1,good_main t2 where t1.gid = t2.id")
    List<HashMap> queryMap();


    List<HashMap> queryAll();

    int check(@Param("entity") GoodKeyWord entity);
}
