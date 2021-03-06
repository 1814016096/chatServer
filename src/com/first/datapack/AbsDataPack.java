package com.first.datapack;

import com.first.Ann.DatePack;
import com.first.plug.AbsType;
import com.first.server.CoreServer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 原初
 * @create 2021 - 11 - 09
 * @version 0.1.0 数据包的抽象类
 */
@DatePack(name = "AbsPack")//抽象的包，所有的数据包都要继承这个
public class AbsDataPack<T> implements Serializable {
    static final long serialVersionUID = 19198L;
    private String startWith;
    public static void setCharSet(String charSet) {
        charSet = charSet;
    }
    public String getStartWith() {
        return startWith;
    }
    public void setStartWith(String startWith) {
        this.startWith = startWith;
    }
    private String Clientname;//客户端名字
    public void setClientname(String clientname) {
        Clientname = clientname;
    }
    public String getName()
    {
        return Clientname;
    }
    private ArrayList<CoreServer> all;//所有的客户端
    private T innerData;//真实的数据
    public void setData(T outData)
    {
        this.innerData = outData;
    }
    public T getInnerData()
    {
        return this.innerData;
    }
    @Override
    public String toString() {
        return innerData.toString();
    }

    private AbsType DataType = AbsType.CORE;
    public AbsType getDataType() {
        return DataType;
    }
    public void setDataType(AbsType dataType) {
        DataType = dataType;
    }
}
