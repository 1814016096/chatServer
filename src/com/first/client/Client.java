package com.first.client;

import com.first.datapack.AbsDataPack;
import com.first.plug.AbsPlug;
import com.first.plug.AbsType;
import com.first.plug.client.AbsClientPlug;
import com.first.plugloader.PlugProcess;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 原初
 * @create 2021 - 11 - 04
 * @version 0.0.1
 */
public class Client {
    public String tempStatement;
    ArrayList<Class<? extends AbsPlug<?>>> clientPlugs;
    ArrayList<AbsClientPlug> tempPlug;
    private boolean thisIsColse = false;
    private Socket cntSot;
    private String name = null;
    private boolean isConnect;
    private String ip;
    private String whatCharSet = "gbk";
    ObjectOutputStream objout = null;
    public String getWhatCharSet() {
        return whatCharSet;
    }

    public ArrayList<AbsClientPlug> getTempPlug() {
        return tempPlug;
    }

    //还没测试
    private Client(){
        File plugDir = new File(System.getProperty("user.dir") + "/chatServe" +"/clientPlug");
        System.out.println(plugDir.getAbsolutePath());
        clientPlugs = new ArrayList<>(50);
        File[] plugs = plugDir.listFiles();
        for(var plug : plugs)
        {
            if(plug.getName().endsWith(".jar"))
            {
                clientPlugs.addAll(PlugProcess.getAllPathPlug(plug.getAbsolutePath(),"ClientPlug"));
            }
        }
        tempPlug = new ArrayList<>(50);
        for(var plugClass : clientPlugs)
        {
            AbsClientPlug clientPlug = null;
            try {
                clientPlug = (AbsClientPlug) plugClass.getConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
            tempPlug.add(clientPlug);
        }
        for(int i = 0; i < tempPlug.size(); i++)
        {
            var inst = tempPlug.get(i);
            inst.whenInit(this);
        }
        if(tempPlug.size() != clientPlugs.size())
        {
            throw new RuntimeException("插件初始化出现错误!");
        }
    }

    public void setTempPlug(ArrayList<AbsClientPlug> tempPlug) {
        this.tempPlug = tempPlug;
    }

    //close未测试
    public void close() {
        if(cntSot != null && cntSot.isConnected())
        {
            AbsDataPack<String> normal  = new AbsDataPack<>();
            normal.setDataType(AbsType.CLOSE);
            try {
                cntSot.sendUrgentData(0xff);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
            if(cntSot.isConnected())
            {
                for(var inst : tempPlug)
                {
                    inst.beforeClose(normal, this);
                }
                OutputStream outputStream = null;
                ObjectOutputStream out = null;
                try {

                    outputStream = cntSot.getOutputStream();
                    out = new ObjectOutputStream(outputStream);
                    out.writeObject(normal);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    thisIsColse = true;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (cntSot != null && !cntSot.isClosed()) {
                        try {
                            cntSot.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            System.exit(0);
        }
        else
        {
            System.exit(0);
        }
    }
    public void receive() {
        try {
            InputStreamReader inputStream =new InputStreamReader(cntSot.getInputStream(),whatCharSet);
            char[] c= new char[10];
            int len = 0;
            while((len = inputStream.read(c)) != -1)
            {
                System.out.print(new String(c, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public Socket getCntSot() {
        return cntSot;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public Client(String ip) {
        this();
        if(ip != "1.1.1.1")
        try {
            this.cntSot = new Socket(InetAddress.getByName(ip),12221);
            OutputStream out = cntSot.getOutputStream();
            objout = new ObjectOutputStream(out);
            AbsDataPack<Object> startPack = new AbsDataPack<>();
            startPack.setDataType(AbsType.START);
            objout.writeObject(startPack);
            isConnect = true;
            this.ip = ip;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("连接失败");
            isConnect = false;
            System.exit(0);
        }
    }
    public ArrayList<AbsClientPlug<?,?>> matchPlug(String startWith)
    {
        AbsType absType = anylizeType(startWith);
        ArrayList<AbsClientPlug<?,?>> plugArrs = new ArrayList<>(5);
        String realStartWith = "";
        if(absType == AbsType.CHAT);
        else
        {
            realStartWith = startWith;
        }
        for(AbsClientPlug plug : tempPlug)
        {
            if(realStartWith.equals(plug.getCtrlName()))
            {
                if(plug.getInitCtrlPackType() != absType)
                {
                    throw new RuntimeException("插件错误! 插件名为:" + plug.getPlugName());
                }
                plugArrs.add(plug);
            }
        }
        if(plugArrs.size() == 0)
        {
            plugArrs = matchPlug("chat");
        }
        else if(plugArrs.size() > 1)
        {
            AbsClientPlug front = plugArrs.get(0);
            for(AbsClientPlug plug : plugArrs)
            {
                if(plug.initFlag.getClass().getName().equals(
                        front.initFlag.getClass().getName()))
                {
                    front = plug;
                }
                else
                {
                    throw new RuntimeException("插件冲突! 具体为 : " + plug.getPlugName() + " 和 "
                            + front.getPlugName());
                }
            }
        }
        else;
        return plugArrs;
    }
    public String getStartWith(String startWith)
    {
        AbsType absType = anylizeType(startWith);
        if(absType == AbsType.CHAT)
        {
            return "";
        }
        else if(absType == AbsType.COMMAND)
        {
            return "/";
        }
        else
        {
            return startWith.substring(0,1);
        }
    }
    public AbsType anylizeType(String startWith)
    {
        if(startWith.length() == 0)
        {
            startWith = "chat";
        }
        String mark = startWith.substring(0,1);
        String[] includeArr = new String[]{"!","@","#", "$", "%", "%", "*"};
        List<String> includes = Arrays.asList(includeArr);
        if("/".equals(mark))
        {
            return AbsType.COMMAND;
        }
        else if(includes.contains(mark))
        {
            return AbsType.PLUG;
        }
        else
        {
            return AbsType.CHAT;
        }
    }
    public List<String> split(String statement)
    {
        String[] temp = statement.split(" ");
        List<String> collect = Arrays.stream(temp).filter(x -> !("".equals(x) || " ".equals(x))).collect(Collectors.toList());
        return collect;

    }
    public String merge(List<String> fragrement)
    {
        StringBuffer temp = new StringBuffer(20);
        for(var ele : fragrement)
        {
            temp.append(ele);
        }
        return temp.toString();//ok
    }
    public void say(String str) {
        String start = getStartWith(str);
        tempStatement = str;
        ArrayList<AbsClientPlug<?, ?>> enablePlugList = matchPlug(start);
        AbsDataPack absDataPack = new AbsDataPack();
        absDataPack.setDataType(anylizeType(start));
        absDataPack.setClientname(name);
        absDataPack.setStartWith(start);
        try
        {
            if(objout == null)
            {
                throw new RuntimeException("连接错误");
            }
            for(int i = 0; i < enablePlugList.size(); i++)
            {
                AbsClientPlug plug = enablePlugList.get(i);
                plug.processPack(absDataPack, this);
            }
            objout.writeObject(absDataPack);
            tempStatement = null;
        } catch (IOException e) {
            try {
                cntSot.sendUrgentData(0xff);
            } catch (IOException ioException) {
                System.out.println("服务端崩了，程序尝试重连");
                try {
                    cntSot = new Socket(ip,12221);
                    if(cntSot.isConnected())
                    {
                        objout.close();
                        objout = new ObjectOutputStream(cntSot.getOutputStream());
                        System.out.println("连接成功");
                    }
                } catch (IOException exception) {
                    System.out.println("连接失败，服务器真的崩了");
                    System.exit(-11);
                }
            }
        }
    }
}
