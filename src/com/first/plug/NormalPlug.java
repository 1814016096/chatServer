package com.first.plug;

import com.first.Ann.ServerPlug;
import com.first.datapack.AbsDataPack;
import com.first.server.CoreServer;

/**
 * @author 原初
 * @create 2021 - 11 - 10
 * @version 0.0.2 一个什么用都没有的默认服务器插件
 */
@ServerPlug(name = "normal")
public class NormalPlug extends AbsServerPlug<String>{
    public NormalPlug() {
        setStatic("","这个插件啥都没做，如果你想的话，也可以继承这个插件"
                ,AbsType.CORE);//自己写插件，在构造器中请先提供你插件的信息
        setFlag("");
    }
    @Override
    public AbsDataPack<String> afterInput() {
        return null;
    }

    @Override
    public boolean filter(CoreServer otherClient) {
        return true;
    }

    @Override
    public void beforeWrite() {

    }

    @Override
    public void afterWriter() {

    }
}
