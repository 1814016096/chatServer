package com.first.plug.server;

import com.first.Ann.ServerPlug;
import com.first.datapack.AbsDataPack;
import com.first.plug.AbsType;
import com.first.server.CoreServer;

/**
 * @author 原初
 * @create 2021 - 11 - 10
 * @version 0.0.2 服务器负责聊天内容的核心组件
 */
@ServerPlug(name = "corechat")
public class CoreChat extends NormalPlug{
    //服务器负责聊天内容的最本质的插件
    public CoreChat()
    {
        setSomting("","聊天插件", AbsType.CHAT);
    }

    @Override
    public AbsDataPack<String> afterInput(CoreServer coreServer) {
        return this.getGettedPack();
    }
}
