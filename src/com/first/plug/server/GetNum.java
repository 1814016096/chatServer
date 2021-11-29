package com.first.plug.server;

import com.first.Ann.ServerPlug;
import com.first.datapack.AbsDataPack;
import com.first.plug.AbsType;
import com.first.server.CoreServer;

import java.util.ArrayList;

/**
 * @author 原初
 * @create 2021 - 11 - 28
 */
@ServerPlug(name = "getNum")
public class GetNum extends NormalPlug{
    public GetNum() {
        setSomting("/num","获取在线人数"
                , AbsType.COMMAND);//自己写插件，在构造器中请先提供你插件的信息
    }
    @Override
    public boolean filter(CoreServer thisClient, ArrayList<CoreServer> others) {
        if(thisClient.getClientName().equals(getGettedPack().getName()))
        {
            return true;
        }
        return false;
    }

    @Override
    public AbsDataPack<String> afterInput(CoreServer coreServer) {
        //AbsDataPack<String> date = new AbsDataPack<>();
        getGettedPack().setDataType(AbsType.CHAT);
        getGettedPack().setStartWith("");
        getGettedPack().setData("在线:" + coreServer.getOthers().size() + "人");
        return getGettedPack();
    }
}
