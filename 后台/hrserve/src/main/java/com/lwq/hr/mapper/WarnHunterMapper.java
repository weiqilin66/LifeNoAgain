package com.lwq.hr.mapper;

import com.lwq.hr.entity.Goods;
import com.lwq.hr.entity.WarnHunter;
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
 * @since 2020-09-25
 */
public interface WarnHunterMapper extends BaseMapper<WarnHunter> {
    @Delete("truncate table warn_hunter")
    void delAll();

    int batchInsert(@Param("list") List<List<Goods>> hunterList);

    List<HashMap> queryAll();
}
