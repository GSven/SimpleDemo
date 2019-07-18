package com.gsven.library.arounter;

/**
 * @author Gsven
 * @Date 2019/4/26 18:10
 * @Desc 路由目录
 *
 * 所有的路基都是必须：MODEL+ ACITVITYNAME
 * 例如app模块的MainActivity -->path="/app/MainActivity"
 */
public class ARouterConstant {


    //跳转到
//    获取当前类名
//    适用于非静态方法：this.getClass().getName()
//    适用于静态方法：Thread.currentThread().getStackTrace()[1].getClassName()
//
    public static final String ACTIVITY_APP_ACTIVITY = "/app/";
    public static final String ACTIVITY_APP_ACTIVITY_MAIN = "/app/MainActivity";
    public static final String ACTIVITY_APP_ACTIVITY_TAOBAO_TWO = "/app/TaoBaoLevelTwoActivity";
    public static final String ACTIVITY_APP_ACTIVITY_RICHEDITOR = "/app/RichEditorActivity";
    public static final String ACTIVITY_APP_ACTIVITY_DEMO = "/app/DemoActivity";
    public static final String ACTIVITY_APP_ACTIVITY_PDF = "/app/PDFActivity";
    public static final String ACTIVITY_APP_ACTIVITY_PALETTE = "/app/PaletteActivity";
    public static final String ACTIVITY_APP_ACTIVITY_TESTCJOIN = "/app/TestCJoinActivity";
    public static final String ACTIVITY_APP_ACTIVITY_TEST_GANK = "/app/TestGankActivity";


}
