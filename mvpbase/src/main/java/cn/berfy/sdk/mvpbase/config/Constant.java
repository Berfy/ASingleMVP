package cn.berfy.sdk.mvpbase.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 公共常量关配置
 *
 * @author NorthStar
 * @date 2018/1/17 16:15
 */
public class Constant {

    //TODO 正式版debug必须设置为false
    public static final boolean DEBUG = true;
    //TODO 数据库版本
    public static final int DB_VERSION = 1;
    //TODO 数据库名称
    public static final String DB_NAME = "DB";
    //TODO 是否启用沉浸式布局
    public static final boolean ENABLE_TRANSLUCENT = true;

    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);//通用
    public static final ExecutorService EXECUTOR_ANIM = Executors.newFixedThreadPool(20);//动画相关
    public static final ExecutorService EXECUTOR_NOTICE = Executors.newFixedThreadPool(20);//通知

}
