package com.first.datapack;

import com.first.Ann.DatePack;
import com.first.plug.AbsType;
import com.first.server.CoreServer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @author 原初
 * @create 2021 - 11 - 09
 * @version 0.0.2 数据包的抽象类
 */
@DatePack(name = "AbsPack")//抽象的包，所有的数据包都要继承这个
public class AbsDataPack<T> implements Serializable {
    static final long serialVersionUID = 19198L;
    private String startWith;
    private static String charSet = "gbk";
    public static String getCharSet() {
        return charSet;
    }
    public static void setCharSet(String charSet) {
        charSet = charSet;
    }
    public String getStartWith() {
        return startWith;
    }
    public void setStartWith(String startWith) {
        this.startWith = startWith;
    }
    private CoreServer whatCoreServer;//是哪个客户端socket发送的数据?
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
    public InputStream getInnerDataByInputStream() throws UnsupportedEncodingException {
        return new ByteArrayInputStream(this.innerData.toString().getBytes(charSet));
    }
    public String getInnerDataBystring()
    {
        return this.innerData.toString();
    }
    private AbsType DataType = AbsType.CORE;
    public AbsType getDataType() {
        return DataType;
    }
    public void setDataType(AbsType dataType) {
        DataType = dataType;
    }
}
