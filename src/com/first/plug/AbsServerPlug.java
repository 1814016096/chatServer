package com.first.plug;

import com.first.Ann.ServerPlug;
import com.first.datapack.AbsDataPack;
import com.first.server.CoreServer;

import java.io.Serializable;

/**
 * @author 原初
 * @create 2021 - 11 - 08
 * @version 0.0.2 抽象的插件类，无论将来是什么插件，都得继承于这个插件
 */
@ServerPlug(name = "AbsPlug")
public abstract class AbsServerPlug<T> implements Serializable {
    static final long serialVersionUID = 114514L;//可序列化的序列号，自觉覆盖这个，
    // 不然我可不能保证传输的时候会发生什么
    public T flag;//啥都没用，只是标识
    public void setFlag(T t)
    {
        flag = t;
    }
    protected String ctrlName = "AbsPack";//你控制的数据包的名字
    protected String description = "这是一个抽象的服务端plug，根本不算入插件列表";
    protected AbsType ctrlplugType = AbsType.CORE;
    public void setStatic(String cName, String dion, AbsType type)
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
    private AbsDataPack<T> gettedPack;//获取的数据包
    public AbsDataPack<T> getGettedPack() {
        return gettedPack;
    }
    public void setGettedPack(AbsDataPack<T> gettedPack) {
        this.gettedPack = gettedPack;
    }
    private String ctrlstartWith;//你控制的语句以什么开始呢?
    public abstract AbsDataPack<T> afterInput();//在接收到目标的内容之后该做的对数据包的处理
    public abstract boolean filter(CoreServer otherClient);//写入的过滤器，过滤掉一些你不想要写入的客户端！
    public abstract void beforeWrite();//写入之前呢?
    public abstract void afterWriter();//在写入之后要做什么
    public String getCltrName() {
        return ctrlstartWith;
    }
}
