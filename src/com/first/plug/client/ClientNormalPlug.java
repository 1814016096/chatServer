package com.first.plug.client;

import com.first.Ann.ClientPlug;
import com.first.client.Client;
import com.first.datapack.AbsDataPack;

/**
 * @author 原初
 * @create 2021 - 11 - 18
 * @version 0.0.2 默认插件，如果没有特别要求，请一律继承这个
 */
@ClientPlug(name = "NormalPlug")
public class ClientNormalPlug extends AbsClientPlug<String, String>{
    @Override
    public void whenInit(Client thisCli) {
    }

    @Override
    public void processPack(AbsDataPack<String> Datapack, Client thisCli) {

    }

    @Override
    public void sendMessage(AbsDataPack<String> Datapack, Client thisCli) {

    }

    @Override
    public void afterSend(AbsDataPack<String> Datapack, Client thisCli) {

    }

    @Override
    public void whenReceive(AbsDataPack<String> Datapack, Client thisCli) {

    }

    @Override
    public void beforeClose(AbsDataPack<String> Datapack, Client thisCli) {

    }
}
