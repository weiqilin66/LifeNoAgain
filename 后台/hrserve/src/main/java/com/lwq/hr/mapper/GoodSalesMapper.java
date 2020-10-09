package com.lwq.hr.mapper;

import com.lwq.hr.entity.GoodSales;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
public interface GoodSalesMapper extends BaseMapper<GoodSales> {

    List<HashMap> selByGid(int gid, String startDate, String endDate);

    float selByGid2(int gid, String nowDate);

}
