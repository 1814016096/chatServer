package com.first.plug.client;

import com.first.Ann.ClientPlug;
import com.first.client.Client;
import com.first.datapack.AbsDataPack;
import com.first.plug.AbsType;

/**
 * @author 原初
 * @create 2021 - 11 - 21
 */
@ClientPlug(name = "CoreChat")
public class ChatCore extends ClientNormalPlug{
    public ChatCore()
    {
        setSomting("", "核心的聊天插件，请置于最上层", AbsType.CHAT);
    }
    @Override
    public void processPack(AbsDataPack<String> Datapack, Client thisCli) {
        Datapack.setData("<" + thisCli.getName() + ">" + thisCli.tempStatement);
    }
}