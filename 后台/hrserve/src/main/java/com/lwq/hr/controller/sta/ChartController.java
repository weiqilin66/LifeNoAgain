package com.lwq.hr.controller.sta;

import com.lwq.hr.entity.*;
import com.lwq.hr.mapper.*;
import com.lwq.hr.service.ChartService;
import com.lwq.hr.utils.MonitorUtil;
import com.lwq.hr.utils.RespBean;

import com.lwq.hr.utils.WayneCheckListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/statistics/chart")
public class ChartController {

    @Resource
    GoodsMapper goodsMapper;
    @Resource
    GoodKeyWordMapper goodKeyWordMapper;
    @Autowired
    ChartService chartService;
    @Resource
    GoodStockMapper goodStockMapper;
    @Resource
    ShopMapper shopmapper;

    @Value("${wayne.goodPath}")
    String filePath;


    @GetMapping("/get2diff")
    public RespBean getMaxMinFromShop(String maxShop, String minShop){

        return RespBean.ok(chartService.get2diff(maxShop,minShop));
    }
    //清除springcache缓存
    @RequestMapping("/remove")
    @CacheEvict(cacheNames = "diff",allEntries = true)
    public String remove(){
        return "清除diff缓存完毕";
    }
    /**
     * @return lwq.returnbean.RespBean
     * @TODO 根据差价再分析 最高价格两张以上的店铺作为坑的目标 / 取集合中对象某个字段相同的对象map.containsKey()
     * @date 2020/5/15
     */
    @GetMapping("/thief")
    public RespBean thiefShop() {
        List<Map<String, Object>> maxMin = getMaxMinToday();
        Map<String, Object> resMap = chartService.getMap(maxMin);

        return RespBean.build().setData(resMap);
    }

    /**
     * 抽取当天差价方法
     */
    private List<Map<String, Object>> getMaxMinToday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String now = dateFormat.format(new Date());
        return chartService.getMaxMin(now,null);
    }

    /**
     * 根据店铺名称二次加工数据
     */
    @GetMapping("/byShop")
    public RespBean byShop(String shopName){
        List<Map<String, Object>> maxMin = getMaxMinToday();
        List<Map<String, Object>> resList = new ArrayList<>();
        for (Map<String, Object> map : maxMin) {
            if (map.get("maxShop").equals(shopName)) {
                resList.add(map);
            }
        }
        return RespBean.build().setData(resList);
    }
    /**
     * @return 读取Excel获取宝贝关键字
     * @date 2020/5/13
     */
    @GetMapping("/goodList")
    public RespBean getGoodList() {
//        List<TbKw> list = tbKwMapper.selAll();
        return RespBean.build().setData(goodKeyWordMapper.queryMap());
    }

    @GetMapping("/initExcel")
    public RespBean initExcelData() throws IOException {
        return chartService.loadExcelData(filePath);
    }
    /**
     * 折现图接口V.2
     */
    @GetMapping("/byTitle2")
    public RespBean getCharts(GoodKeyWord vo){

        return RespBean.build().setMessage("ok").setData(chartService.getCharts(vo));
    }
    /**
     * @TODO 折线图接口
     * @date 2020/5/13
     */
    @GetMapping("/byTitle")
    public Map<String, Object> byTitle(int id, String startDate,String endDate) {
        Map<String, Object> resMap = new HashMap<>();
        List<Goods> resList = new ArrayList<>();
        List<String> errorList = Collections.synchronizedList(new ArrayList<>());

        // 检索关键字优化
        final GoodKeyWord goodKeyWord = goodKeyWordMapper.selectById(id);
        int gid =goodKeyWord.getGid();
        List<Shop> shops = shopmapper.selectAll();// 统计名店
        resMap.put("shops", shops);//图例店铺名
        String [] xAxis = MonitorUtil.getTimes(startDate, endDate);
        shops.parallelStream().forEach(item->{
            String shop = item.getName();//旺旺名,大数据过滤用
            String title = item.getComment();
            List<Goods> goods = goodsMapper.byKeyWord(goodKeyWord, shop, startDate, endDate);
            List<String> times = new ArrayList<>();//所有日期
            for (Goods good : goods) {
                times.add(good.getEtlDate());
            }
            if (WayneCheckListUtil.checkRepeat(times)) {
                errorList.add("店铺:["+shop+"]存在2天及以上重复数据,检查关键词");
                resMap.put("error",errorList);
                return;
            }
            // 得到两个日期数组差异 加工没获取到数据price为0
            List<String> newList = getDIffList(Arrays.asList(xAxis), times);
            for (String s : newList) {
                Goods good = new Goods();
                good.setEtlDate(s);
                good.setPrice((float) 0);
                goods.add(good);
            }
            // 根据etl_date重新排序集合 n天的宝贝
            Collections.sort(goods, new Comparator<Goods>() {
                @Override
                public int compare(Goods o1, Goods o2) {
                    //return o1.getEtlDate().compareTo(o2.getEtlDate()); //升序
                    return o2.getEtlDate().compareTo(o1.getEtlDate()); //降序
                }
            });
            float[] prices = new float[xAxis.length];
            for (int j = 0; j < prices.length; j++) {
                prices[j] = goods.get(j).getPrice();
            }

            resMap.put(title, prices);
            // 最新一天的对象存入详情表格
            if (goods.get(0).getPrice()!=0) {
                resList.add(goods.get(0));
            }
        });
        //按销量排序下 (字符串用compareTo 数值用－)
        Collections.sort(resList, new Comparator<Goods>() {
            @Override
            public int compare(Goods o1, Goods o2) {
                return o2.getSales()-o1.getSales(); //降序
            }
        });
        resMap.put("objList",resList);
        resMap.put("stockTable",goodStockMapper.queryByGid(gid));
        return resMap;
    }

    /**
     * @TODO 查当日价格差超过20的宝贝
     * @date 2020/3/11
     */
    @GetMapping("/maxAndMin")
    public RespBean getMaxAndMin(String shop) {
        // 日期字符串
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String now = dateFormat.format(new Date());
        List<Goods> total = goodsMapper.check(now);
        if (total.size() == 0) {
            return RespBean.error("今天还没爬取数据!");
        }
        List<Map<String, Object>> maxMin = null;
        if ("".equals(shop) || shop==null) {
            maxMin = chartService.getMaxMin(now,null);
        }else {
            maxMin = chartService.getMaxMin(now,shop);
        }
        if (maxMin.get(0).get("error") != null) {
            return RespBean.error(maxMin.get(0).get("error").toString());
        }
        return RespBean.build().setData(maxMin);
    }


    /**
     * @return java.util.Date
     * @TODO 获取近days-1天的日期
     * @date 2020/5/12
     */
    private static Date getDateAdd(int days) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -days);
        return c.getTime();
    }

    private static List<String> getDaysBetwwen(int days) { //最近几天日期
        List<String> dayss = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(getDateAdd(days));
        Long startTIme = start.getTimeInMillis();
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
        Long endTime = end.getTimeInMillis();
        Long oneDay = 1000 * 60 * 60 * 24l;
        Long time = startTIme;
        while (time <= endTime) {
            Date d = new Date(time);
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
//            System.out.println(df.format(d));
            dayss.add(df.format(d));
            time += oneDay;
        }
        return dayss;
    }

    /**
     * @param
     * @return
     * @TODO 快速比较两个集合的差异(主键)
     * @date 2020/5/12
     */
    private List<String> getDIffList(List<String> allOpenidList, List<String> dbOpenidList) {
        if (dbOpenidList != null && !dbOpenidList.isEmpty()) {
            Map<String, String> dataMap = new HashMap<String, String>();
            for (String id : dbOpenidList) {
                dataMap.put(id, id);
            }

            List<String> newList = new ArrayList<String>();
            for (String id : allOpenidList) {
                if (!dataMap.containsKey(id)) {
                    newList.add(id);
                }
            }
            return newList;
        } else {
            return allOpenidList;
        }
    }

}
