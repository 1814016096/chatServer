package com.first.datapack;

import com.first.plug.AbsType;

/**
 * @author 原初
 * @create 2021 - 11 - 13
 * @version 0.0.2 聊天类的数据包
 */
public class ChatDataPack extends AbsDataPack<String>{
    public ChatDataPack()
    {
        setDataType(AbsType.CHAT);
        setStartWith("");
        //test:
        setData("我超");
    }
    @Override
    public String toCoreServe() {
        return null;
    }
}
