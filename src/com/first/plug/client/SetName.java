package com.first.plug.client;

import com.first.Ann.ClientPlug;
import com.first.client.Client;
import com.first.plug.AbsType;

import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author 原初
 * @create 2021 - 11 - 21
 * @version 0.1.0 客户端获得明知道插件
 */
@ClientPlug(name = "GetName")
public class SetName extends ClientNormalPlug{
    static final long serialVersionUID = 841521L;
    public SetName()
    {
        setSomting("null", "让客户端获取用户名字", AbsType.CORE);
    }
    private static Consumer<String> setPrintType = null;
    private static Supplier<String> setInputType = null;
    public static void setType(Consumer<String> setPrintType, Supplier<String> setInputType) {
        SetName.setPrintType = setPrintType;
        SetName.setInputType = setInputType;
    }
    public static void setPrintWay(Consumer<String> setPrintType)
    {
        SetName.setPrintType = setPrintType;
    }
    public static void setInputWay(Supplier<String> setInputType)
    {
        SetName.setInputType = setInputType;
    }
    @Override
    public void whenInit(Client thisCli) {
        if(thisCli.getName() == null)
        {
            Scanner scan = new Scanner(System.in);
            if(setPrintType == null || setInputType == null)
            {
                System.out.println("请输入姓名:");
                String name = scan.next();
                thisCli.setName(name);
            }
            else
            {
                setPrintType.accept("请输入姓名:");
                String name = setInputType.get();
                thisCli.setName(name);
            }
        }
    }
}
