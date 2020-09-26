package com.lwq.hr.mapper;

import com.lwq.hr.entity.Goods;
import com.lwq.hr.entity.WarnLowerPrice;
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
public interface WarnLowerPriceMapper extends BaseMapper<WarnLowerPrice> {

    @Delete("truncate table warn_lower_price")
    void delAll();

    void batchInsert(@Param("list") List<List<Goods>> resList);

    List<HashMap> queryAll();
}
