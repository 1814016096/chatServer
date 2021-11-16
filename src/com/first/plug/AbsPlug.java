package com.first.plug;

import java.io.Serializable;

/**
 * @author 原初
 * @create 2021 - 11 - 16
 * @version 0.0.2 抽象的插件类
 */
public abstract class AbsPlug <T> implements Serializable {
    static final long serialVersionUID = 1145L;//可序列化的序列号，自觉覆盖这个，
    // 不然我可不能保证传输的时候会发生什么
    public T flag;//啥都没用，只是标识
    public void setFlag(T t)
    {
        flag = t;
    }
    protected String ctrlName = "Abs";//你控制的数据包的名字
    protected String description = "这是一个抽象的plug，根本不算入插件列表";
    protected AbsType ctrlplugType = AbsType.CORE;
    public void setSomting(String cName, String dion, AbsType type)
    {
        ctrlName = cName;
        description = dion;
        ctrlplugType = type;
    }
    public String getCtrlName()
    {
        return ctrlName;
    }
    public String getDescription() {
        return description;
    }
    public AbsType getCtrlplugType() {
        return ctrlplugType;
    }
    private String ctrlstartWith;//你控制的语句以什么开始呢?
    public String getCltrName() {
        return ctrlstartWith;
    }
}
