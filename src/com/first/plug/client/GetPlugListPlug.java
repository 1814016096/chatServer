package com.first.plug.client;

import com.first.Ann.ClientPlug;
import com.first.client.Client;
import com.first.datapack.AbsDataPack;
import com.first.plug.AbsType;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @author 原初
 * @create 2021 - 11 - 19
 * @version 0.0.2 获得插件列表
 */
@ClientPlug(name = "getPlugs")
public class GetPlugListPlug extends ClientNormalPlug{
    private static Consumer<String> printWay = x -> System.out.println(x);
    public static void setPrintWay(Consumer<String> printWay)
    {
        GetPlugListPlug.printWay = printWay;
    }
    public GetPlugListPlug()
    {
        setSomting("/plug","一个获取插件列表的插件", AbsType.COMMAND);
        setInitCtrlPackType(AbsType.COMMAND);
        isLocal = true;
    }

    @Override
    public void processPack(AbsDataPack<String> Datapack, Client thisCli) {
        whenInit(thisCli);
    }

    @Override
    public void whenInit(Client thisCli) {
        ArrayList<AbsClientPlug> tempPlugs = thisCli.getTempPlug();
        printWay.accept("正在使用的插件:");
        for(var plug : tempPlugs)
        {
            printWay.accept(plug.getPlugName() + "(" + plug.getCtrlplugType() + ")" + " : " + plug.getDescription());
        }
    }
}
