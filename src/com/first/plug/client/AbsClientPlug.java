package com.first.plug.client;

import com.first.Ann.ClientPlug;
import com.first.client.Client;
import com.first.datapack.AbsDataPack;
import com.first.plug.AbsPlug;
import com.first.plug.AbsType;

/**
 * @author 原初
 * @create 2021 - 11 - 16
 * @version 0.0.2 抽象的客户端插件
 */
@ClientPlug(name = "AbsPlug")
public abstract class AbsClientPlug<R,T> extends AbsPlug<T> {
    static final long serialVersionUID = 11451L;
    public String getPlugName()
    {
        return this.getClass().getAnnotation(ClientPlug.class).name();
    }
    protected boolean isLocal;//要不要发送数据到服务器
    public boolean isLocal() {
        return isLocal;
    }
    private AbsType initCtrlPackType;//开始时获取的数据包类型
    public AbsType getInitCtrlPackType() {
        return initCtrlPackType;
    }
    public void setInitCtrlPackType(AbsType initCtrlPackType) {
        this.initCtrlPackType = initCtrlPackType;
    }
    public R initFlag;//开始时处理的数据包
    //流程 : 初始化->解析命令、获取相应的插件->根据插件生成相对应的数据包->处理数据包->发送数据包
    //接受数据包->根据数据包类型和startWith获取相应的插件(为啥开始和结束不一致，因为发送和接受到的数据包类型可能
    // 不一致)->插件解析数据包->数据包处理发送命令
    public AbsClientPlug() {
        setSomting("", "一个抽象的客户端插件类，所有的客户端插件都得继承这个", AbsType.CORE);
        isLocal = true;
    }
    abstract public void whenInit(Client thisCli);//初始化的时候做点啥（注意，所有插件初始化时都会执行）
    abstract public void processPack(AbsDataPack<T> Datapack, Client thisCli);//在发送前处理数据包
    //在开始时只能根据startWith判断
    abstract public void sendMessage(AbsDataPack<T> Datapack, Client thisCli);//发送信息
    abstract public void afterSend(AbsDataPack<T> Datapack, Client thisCli);//发送信息后要做啥
    abstract public void whenReceive(AbsDataPack<T> Datapack, Client thisCli);//接受到信息之后
    abstract public void beforeClose(AbsDataPack<T> Datapack, Client thisCli);//在关闭客户端之前要做什么呢(接收到的是结束数据包)
}
