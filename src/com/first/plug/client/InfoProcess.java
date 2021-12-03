package com.first.plug.client;

import com.first.Ann.ClientPlug;
import com.first.client.Client;
import com.first.datapack.AbsDataPack;
import com.first.plug.AbsType;

import java.util.function.Consumer;

/**
 * @author 原初
 * @create 2021 - 12 - 02
 */
@ClientPlug(name = "InfoProcess")
public class InfoProcess extends ClientNormalPlug{
    public InfoProcess()
    {
        setSomting("", "处理INFO信息", AbsType.INFO);
        isLocal = true;
    }

    @Override
    public Consumer<AbsDataPack> whenReceive(AbsDataPack<String> Datapack, Client thisCli) {
        Datapack.setData("[INFO]:"+ Datapack.getInnerData());
        return null;
    }
}
