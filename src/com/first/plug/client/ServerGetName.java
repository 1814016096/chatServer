package com.first.plug.client;

import com.first.Ann.ClientPlug;
import com.first.client.Client;
import com.first.plug.AbsType;

/**
 * @author 原初
 * @create 2021 - 11 - 27
 */
@ClientPlug(name = "ServerName")
public class ServerGetName extends ClientNormalPlug {
    static final long serialVersionUID = -1206L;
    public ServerGetName()
    {
        setSomting("null", "一个为服务器设置名字的插件", AbsType.CORE);
    }
    @Override
    public void whenInit(Client thisCli) {
        thisCli.setName("server");
    }
}
