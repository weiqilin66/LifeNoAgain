package com.lwq.hr.mapper;

import com.lwq.hr.entity.WarnKeywordNoset;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LinWeiQi
 * @since 2020-09-24
 */
public interface WarnKeywordNosetMapper extends BaseMapper<WarnKeywordNoset> {
    @Delete("truncate table warn_keyword_noset")
    void delAll();

    void batchInsert(@Param("list") List<HashMap> noKeyWordList);
}
