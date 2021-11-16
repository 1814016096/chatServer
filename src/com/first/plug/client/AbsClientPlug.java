package com.first.plug.client;

import com.first.Ann.ClientPlug;
import com.first.plug.AbsPlug;
import com.first.plug.AbsType;

/**
 * @author 原初
 * @create 2021 - 11 - 16
 * @version 0.0.2 抽象的客户端插件
 */
@ClientPlug(name = "AbsPlug")
public class AbsClientPlug<T> extends AbsPlug<T> {
    public AbsClientPlug() {
        setSomting("", "一个抽象的客户端插件类，所有的客户端插件都得继承这个", AbsType.CORE);
    }
}
