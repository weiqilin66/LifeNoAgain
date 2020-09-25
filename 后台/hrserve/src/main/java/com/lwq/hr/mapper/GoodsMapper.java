package com.lwq.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lwq.hr.entity.GoodKeyWord;
import com.lwq.hr.entity.Goods;
import com.lwq.hr.entity.Shop;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LinWeiQi
 * @since 2020-03-09
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("select count(*) from goods")
    long selectAll();

    List<Goods> byTitle(String shop, String title, List<String> days, String condition);

    List<Goods> getMax(String now, String goodName, String condition,List<Shop> list);

    List<Goods> getMin(String now, String goodName, String condition,List<Shop> list);

    List<Goods> check(String date);

    List<Goods> getMaxMinFromSHop(String shop,String goodName, String condition);

    List<Goods> byKeyWord(@Param("vo") GoodKeyWord vo, @Param("shopName")String shopName,
                          @Param("beginDate") String now, @Param("endDate") String endDate);

    List<Goods> selWarningLower(@Param("date") String date, @Param("list")List<String> shopList,
                               @Param("gid")int gid, @Param("price")float price, @Param("base")String base,
                               @Param("include1")String include1, @Param("include2")String include2,
                               @Param("include3")String include3, @Param("enclude1")String enclude1,
                               @Param("enclude2")String enclude2, @Param("enclude3")String enclude3);

    @Select("select max(etl_date) from goods")
    String selMaxDate();

    List<Goods> selHunter(String date, List<String> hunterShops, int gid, float price, String base, String include1, String include2, String include3, String enclude1, String enclude2, String enclude3);
}
