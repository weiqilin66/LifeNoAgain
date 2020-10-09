package com.lwq.hr.mapper;

import com.lwq.hr.entity.CoreCrawlTb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
public interface CoreCrawlTbMapper extends BaseMapper<CoreCrawlTb> {

    List<HashMap> queryAll();

}
