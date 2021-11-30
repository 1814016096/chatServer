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
 * @create 2021 - 11 - 30
 */
@ServerPlug(name = "closeClient")
public class CloseToClient extends NormalPlug{
    private boolean enable = false;
    String clientName = null;
    public CloseToClient()
    {
        setSomting("/close", "关闭指定客户端，只能在服务器上发送相应命令", AbsType.COMMAND);
    }

    @Override
    public AbsDataPack<String> afterInput(CoreServer coreServer) {
        if(coreServer.getClientName().equals("server"))
        {
            List<String> split = Client.split(getGettedPack().toString());
            try{
                clientName = split.get(1);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            enable = true;
        }
        return getGettedPack();
    }

    @Override
    public boolean filter(CoreServer otherClient, ArrayList<CoreServer> others) {
        for(var client : others)
        {
            if(client.getClientName().equals(clientName))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void beforeWrite(CoreServer whatSocket) {
        getGettedPack().setDataType(AbsType.CLOSE);
    }
}
