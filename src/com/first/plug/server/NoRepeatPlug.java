package com.first.plug.server;

import com.first.Ann.ServerPlug;
import com.first.datapack.AbsDataPack;
import com.first.plug.AbsType;
import com.first.server.CoreServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 原初
 * @create 2021 - 12 - 02
 */
@ServerPlug(name = "NoRepeat")
public class NoRepeatPlug extends NormalPlug{
    Map<String, CoreServer> nameMap;

    @Override
    public void whenClientDisconnection(CoreServer disServer) {
        nameMap.remove(disServer.getClientName());
    }

    public NoRepeatPlug()
    {
        setSomting("null", "不允许客户端名字重复", AbsType.CORE);
        nameMap = new HashMap<>(50);
    }
    @Override
    public void afterReceive(CoreServer whatSocket) {
        if(nameMap.containsKey(whatSocket.getClientName()))
        {
            ObjectOutputStream out = whatSocket.getAllOut().get(whatSocket);
            AbsDataPack<String> print = new AbsDataPack<>();
            print.setDataType(AbsType.INFO);
            print.setData("你名字和服务器里面的人重了");
            print.setStartWith("");
            AbsDataPack<String> closeClient = new AbsDataPack<>();
            closeClient.setDataType(AbsType.CLOSE);
            try {
                out.writeObject(print);
                out.writeObject(closeClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            nameMap.put(whatSocket.getClientName(),whatSocket);
        }
    }
}
