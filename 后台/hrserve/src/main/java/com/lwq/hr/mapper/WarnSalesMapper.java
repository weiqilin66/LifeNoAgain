package com.lwq.hr.mapper;

import com.lwq.hr.entity.GoodSales;
import com.lwq.hr.entity.WarnSales;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
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
 * @since 2020-10-09
 */
public interface WarnSalesMapper extends BaseMapper<WarnSales> {

    @Delete("truncate table warn_sales")
    int delAll();

    void batchInsert(@Param("list") List<WarnSales> list);


    List<HashMap> queryAll();
}
