package com.lwq.hr.generator;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.lwq.hr.utils.DataSourceProperties;
import com.lwq.hr.utils.scannerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 自动化
 * @author: LinWeiQi
 */
public class mapperGenerator {

    public void run() {
        /**
         * 代码生成器
         */
        AutoGenerator mpg = new AutoGenerator();

        /**
         * 全局配置
         */
        GlobalConfig globalConfig = new GlobalConfig();
        PackageConfig pc = new PackageConfig();
        //生成文件的输出目录
        String projectPath = System.getProperty("user.dir");//工程根目录  xx/mService
        String projectPath2;
        String sonProjectPath = scannerUtil.sc("子工程路径");
        if (sonProjectPath.equals("default")) {
            /*
                输入default 自动在hrserve下生产mbg
             */
            projectPath2 = projectPath + "//" + "service-main/hrserve";

            pc.setParent("com.lwq.hr");
            mpg.setPackageInfo(pc);
        } else {
            // 路径配置
            projectPath2 = projectPath + "//" + sonProjectPath;
            System.out.println("子工程根路径 :" + projectPath2 + "\n");
            /**
             * 包配置
             */
            pc = new PackageConfig();
            pc.setParent(scannerUtil.sc("主包名 (例如:com.sys 则会再com.sys下的entity/mapper/service/controller文件夹生成文件)"));
            mpg.setPackageInfo(pc);
        }

        globalConfig.setOutputDir(projectPath2 + "/src/main/java");
        //Author设置作者
        globalConfig.setAuthor("LinWeiQi");
        //是否覆盖文件
        globalConfig.setFileOverride(true);
        //生成后打开文件
        globalConfig.setOpen(false);
        mpg.setGlobalConfig(globalConfig);

        /**
         * 数据源配置
         */
        com.baomidou.mybatisplus.generator.config.DataSourceConfig dataSourceConfig = new com.baomidou.mybatisplus.generator.config.DataSourceConfig();
        // 数据库类型,默认MYSQL
        dataSourceConfig.setDbType(DbType.MYSQL);
        //自定义数据类型转换
        dataSourceConfig.setTypeConvert(new MySqlTypeConvert());
        dataSourceConfig.setUrl(DataSourceProperties.URL);
        dataSourceConfig.setDriverName(DataSourceProperties.DRIVER);
        dataSourceConfig.setUsername(DataSourceProperties.USERNAME);
        dataSourceConfig.setPassword(DataSourceProperties.PASSWORD);
        mpg.setDataSource(dataSourceConfig);


        /**
         * 自定义配置
         */
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        /**
         * 模板
         */
        //如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";


        /**
         * 自定义输出配置
         */
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return  "/src/main/resources/mapper/" //+ pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        /**
         * 配置模板
         */
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(null);
        // 配置不生成controller
        templateConfig.setController("");
        //不生成service
        templateConfig.setService("");
        //不生成serviceImpl
        templateConfig.setServiceImpl("");
        mpg.setTemplate(templateConfig);

        /**
         * 策略配置
         */
        StrategyConfig strategy = new StrategyConfig();
        //设置命名格式
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setInclude(scannerUtil.sc("表名,多个英文逗号分割").split(","));
        //实体是否为lombok模型（默认 false）
        strategy.setEntityLombokModel(true);
        //生成 @RestController 控制器
        strategy.setRestControllerStyle(true);
        //设置自定义继承的Entity类全称，带包名
        //strategy.setSuperEntityClass("lwq.base.BaseEntity");
        //设置自定义继承的Controller类全称，带包名
        //strategy.setSuperControllerClass("lwq.base.BaseController");
        //设置自定义基础的Entity类，公共字段
        //strategy.setSuperEntityColumns("id");
        strategy.setEntitySerialVersionUID(false);
        //驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        //表名前缀
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    public static void main(String[] args) {

        new mapperGenerator().run();
    }

}