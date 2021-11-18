package com.first.plug.client;

import com.first.Ann.ClientPlug;
import com.first.client.Client;
import com.first.datapack.AbsDataPack;

/**
 * @author 原初
 * @create 2021 - 11 - 18
 */
@ClientPlug(name = "NormalPlug")
public class ClientNormalPlug extends AbsClientPlug<String, String>{
    @Override
    public void whenInit(Client thisCli) {
        System.out.println("欢迎使用!" + "\n这里是:" +this.getClass()
                .getAnnotation(ClientPlug.class).name() + " 插件");
    }

    @Override
    public void ProcessPack(AbsDataPack<String> Datapack, Client thisCli) {

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
}
