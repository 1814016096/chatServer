package com.first.plug.server;

import com.first.Ann.ServerPlug;
import com.first.datapack.AbsDataPack;
import com.first.plug.AbsType;
import com.first.server.CoreServer;
import com.first.server.ServerManager;

/**
 * @author 原初
 * @create 2021 - 11 - 10
 * @version 0.0.2 一个什么用都没有的默认服务器插件
 */
@ServerPlug(name = "normal")
public class NormalPlug extends AbsServerPlug<String>{
    public NormalPlug() {
        setSomting("","这个插件啥都没做，如果你想的话，也可以继承这个插件"
                , AbsType.CORE);//自己写插件，在构造器中请先提供你插件的信息
        setFlag("");
    }
    @Override
    public void whenInit(ServerManager manager) {

    }

    @Override
    public AbsDataPack<String> afterInput(CoreServer whatSocket, AbsDataPack<String> gettedPack) {
        return null;
    }

    @Override
    public boolean filter(CoreServer otherClient) {
        return true;
    }

    @Override
    public void beforeWrite(CoreServer whatSocket) {

    }

    @Override
    public void afterWriter(CoreServer whatSocket) {

    }

}
