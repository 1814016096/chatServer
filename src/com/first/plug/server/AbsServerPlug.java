package com.first.plug.server;

import com.first.Ann.ServerPlug;
import com.first.datapack.AbsDataPack;
import com.first.plug.AbsPlug;
import com.first.server.CoreServer;
import com.first.server.ServerManager;

import java.util.ArrayList;

/**
 * @author 原初
 * @create 2021 - 11 - 08
 * @version 0.0.2 抽象的插件类，无论将来是什么插件，都得继承于这个插件
 */
@ServerPlug(name = "AbsPlug")
public abstract class AbsServerPlug<T> extends AbsPlug<T>{
    public String getPlugName()
    {
        return this.getClass().getAnnotation(ServerPlug.class).name();
    }
    static final long serialVersionUID = 114514L;
    private AbsDataPack<T> gettedPack;//获取的数据包
    public AbsDataPack<T> getGettedPack() {
        return gettedPack;
    }
    public abstract void whenInit(ServerManager manager);
    public void setGettedPack(AbsDataPack<T> gettedPack) {
        this.gettedPack = gettedPack;
    }
    public abstract AbsDataPack<T> afterInput(CoreServer whatSocket);//在接收到目标的内容之后该做的对数据包的处理
    public abstract boolean filter(CoreServer thisSocket, ArrayList<CoreServer> others);//写入的过滤器，过滤掉一些你不想要写入的客户端！
    public abstract void beforeWrite(CoreServer whatSocket);//写入之前呢?
    public abstract void afterWriter(CoreServer whatSocket);//在写入之后要做什么
    public abstract void afterReceive(CoreServer whatSocket);//在收到客户端socket之后做些什么呢
    public abstract void whenClientDisconnection(CoreServer disServer);//客户端失去链接时
}
