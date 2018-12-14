package com.feng.wanandroid.utils;

import com.feng.wanandroid.entity.eventbus.Event;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/14
 */
public class EventBusUtil {

    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);    //后续注册的订阅者依然可以从内存中得到这个已发送的sticky事件
    }
}

