package com.lwq.hr.mapper;

import com.lwq.hr.entity.GoodKeyWord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
