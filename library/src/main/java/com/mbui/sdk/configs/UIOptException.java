package com.mbui.sdk.configs;

/**
 * Created by chenwei on 15/1/9.
 */

/**
 * 定义不是很严重的异常
 */
public class UIOptException extends Exception {
    public UIOptException() {
        super("UIOptException");
    }

    public UIOptException(String message) {        //用来创建指定参数对象
        super(message);                             //调用超类构造器
    }
}
