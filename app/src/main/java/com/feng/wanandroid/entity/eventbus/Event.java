package com.feng.wanandroid.entity.eventbus;

/**
 * @author Feng Zhaohao
 * Created on 2018/12/14
 */
public class Event<T> {

    private int code;   //该字段用于判断事件的发送源
    private T data;     //事件类型

    public Event(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}