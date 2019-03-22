package com.feng.wanandroid.config;

import com.feng.wanandroid.R;

/**
 * 常量
 *
 * @author Feng Zhaohao
 * Created on 2018/12/7
 */
public class Constant {
    public static final String[] TAB_TEXT = {"首页", "体系", "导航", "项目"};   //各个tab对应的文字

//    public static final int[] TAB_COLOR = {R.color.color_tab_one, R.color.color_tab_two,
//        R.color.color_tab_three, R.color.color_tab_four};   //各个tab对应的底部tab和toolbar的颜色
//
//    public static final int[] STATE_BAR_COLOR = {R.color.color_state_bar_one, R.color.color_state_bar_two,
//            R.color.color_state_bar_three, R.color.color_state_bar_four};   //各个tab对应的状态栏的颜色
    public static final int[] TAB_COLOR = {R.color.color_tab_one, R.color.color_tab_one,
        R.color.color_tab_one, R.color.color_tab_one};   //各个tab对应的toolbar的颜色

    public static final int[] STATE_BAR_COLOR = {R.color.color_state_bar_one, R.color.color_state_bar_one,
            R.color.color_state_bar_one, R.color.color_state_bar_one};   //各个tab对应的状态栏的颜色

    public static final String BASE_URL = "https://www.wanandroid.com/";

    //SP相关
    public static final String COOKIES_SHARE_PRE = "cookies_share_pre";
    public static final String COOKIES_KEY = "cookies_key";
    public static final String AUTO_LOGIN_SHARE_PRE = "auto_login_share_pre";
    public static final String USER_NAME_KEY = "user_name_key";
    public static final String PASSWORD_KEY = "password_key";
    public static final String LOGIN_STATE_SHARE_PRE = "login_state_share_pre";
    public static final String IS_LOGIN_KEY = "is_login_key";

    public static final String ERROR_MSG = "服务器不给力o(╥﹏╥)o";     //服务器获取数据失败时的提示信息

    public static final int HOME_ARTICLE_PAGE_SIZE = 20;    //每页的文章数
}
