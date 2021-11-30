package com.first.plug.client;

import com.first.Ann.ClientPlug;
import com.first.client.Client;
import com.first.datapack.AbsDataPack;
import com.first.plug.AbsType;

import java.util.function.Consumer;

/**
 * @author 原初
 * @create 2021 - 11 - 30
 */
@ClientPlug(name = "clientClose")
public class CloseClient extends ClientNormalPlug{
    public CloseClient()
    {
        setSomting("/close", "关闭客户端", AbsType.COMMAND);
        setInitCtrlPackType(AbsType.COMMAND);
        isLocal = true;
    }

    @Override
    public void processPack(AbsDataPack<String> Datapack, Client thisCli) {
        thisCli.close();
    }

    @Override
    public Consumer<AbsDataPack> whenReceive(AbsDataPack<String> Datapack, Client thisCli) {
        thisCli.close();
        return null;
    }
}
