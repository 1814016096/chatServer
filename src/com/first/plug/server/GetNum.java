package com.first.plug.server;

import com.first.Ann.ServerPlug;
import com.first.client.Client;
import com.first.datapack.AbsDataPack;
import com.first.plug.AbsType;
import com.first.server.CoreServer;

import java.util.ArrayList;
import java.util.List;

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
        List<String> statement = Client.split(getGettedPack().toString());
        StringBuilder detail = new StringBuilder("在线:" + coreServer.getOthers().size() + "人");
        if(statement.size() == 2 && "-d".equals(statement.get(1)))
        {
            detail.append("\n分别为:");
            for(var people : coreServer.getOthers())
            {
                detail.append("\n" + people.getClientName());
            }
        }
        getGettedPack().setDataType(AbsType.INFO);
        getGettedPack().setStartWith("");
        getGettedPack().setData(detail.toString());
        return getGettedPack();
    }
}
