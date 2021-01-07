package com.uuuuuuuuuuuuuuu.model;


import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


/*
    数据层内容生成
 */
public class MybatisPlusGenerator {

    // 生成输出目录，定位到工程的java目录下
    private String outputDir = "D:\\mybatis-plus-gen\\forum\\project\\src\\main\\java";
    // 生成类的作者
    private String author = "juquansheng";
    // 数据源相关配置

    private String url = "jdbc:mysql://localhost:3306/forum?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&serverTimezone=UTC";

    private String driverName = "com.mysql.cj.jdbc.Driver";
    private String userName = "root";
    //private String userPwd = "Mxd!@#";
    private String userPwd = "123456";
    // DAO的包路径
    private String entityPackage = "com.uuuuuuuuuuuuuuu.model";
    private String mapperPackage = "com.uuuuuuuuuuuuuuu.model";
    private String mapperXmlPackage = "com.uuuuuuuuuuuuuuu.model.mapper";
    // 待生成的表名，注意是覆盖更新
    private static String[] tableNames;
    // "galaxy","earth","country_config","city_area","administrative_conifg","grid_category","online_grid"
    static{
        tableNames = new String[]{
                "version_control"
        };
    }


    @Test
    public void MybatisPlusGenerator() {
        AutoGenerator mpg = new AutoGenerator();
        mpg.setTemplateEngine(new BeetlTemplateEngine());
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(outputDir);
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(false);
        gc.setAuthor(author);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(url);
        // dsc.setSchemaName("public");
        dsc.setDriverName(driverName);
        dsc.setUsername(userName);
        dsc.setPassword(userPwd);
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //strategy.setTablePrefix(new String[]{"_"});// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(tableNames);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(null);
        pc.setEntity(entityPackage+".entity");
        pc.setMapper(mapperPackage+".mapper");
        pc.setXml(mapperXmlPackage+".mapper.xml");
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };

        mpg.setCfg(cfg);

        // 执行生成
        mpg.execute();

        // 打印注入设置
        System.err.println(mpg.getCfg().getMap().get("abc"));

    }

}
