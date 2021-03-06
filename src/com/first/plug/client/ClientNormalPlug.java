package com.first.plug.client;

import com.first.Ann.ClientPlug;
import com.first.client.Client;
import com.first.datapack.AbsDataPack;
import com.first.plug.AbsType;

import java.util.function.Consumer;

/**
 * @author 原初
 * @create 2021 - 11 - 18
 * @version 0.0.2 默认插件，如果没有特别要求，请一律继承这个
 */
@ClientPlug(name = "NormalPlug")
public class ClientNormalPlug extends AbsClientPlug<String, String>{
    static final long serialVersionUID = 15102L;
    public ClientNormalPlug()
    {
        initFlag = "un";
        flag = "date";
        setSomting("", "一个默认插件", AbsType.CHAT);
        setInitCtrlPackType(AbsType.CHAT);
        isLocal = true;
    }
    @Override
    public void whenInit(Client thisCli) {
    }

    @Override
    public void processPack(AbsDataPack<String> Datapack, Client thisCli) {

    }


    @Override
    public void afterSend(AbsDataPack<String> Datapack, Client thisCli) {

    }

    @Override
    public Consumer<AbsDataPack> whenReceive(AbsDataPack<String> Datapack, Client thisCli) {
        return null;
    }

    @Override
    public void beforeClose(AbsDataPack<String> Datapack, Client thisCli) {

    }
}
