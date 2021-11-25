package com.first.client;

import com.first.datapack.AbsDataPack;
import com.first.plug.AbsPlug;
import com.first.plug.AbsType;
import com.first.plug.client.AbsClientPlug;
import com.first.plugloader.PlugProcess;

import java.io.*;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;
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
    private Consumer<String> allPrintWay = System.out::println;
    private Supplier<String> allInputWay = () ->{
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    };

    ObjectOutputStream objout = null;
    public String getWhatCharSet() {
        return whatCharSet;
    }

    public ArrayList<AbsClientPlug> getTempPlug() {
        return tempPlug;
    }

    private Client(Consumer<String> printWay, Supplier<String> inputWay){
        if(printWay != null)
        {
            this.allPrintWay = printWay;
        }
        if(inputWay != null)
        {
            this.allInputWay = inputWay;
        }
        AbsDataPack.setCharSet(whatCharSet);
        File plugDir = new File(System.getProperty("user.dir") + "/chatServe" +"/clientPlug");
        //System.out.println(plugDir.getAbsolutePath());
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
                Method met = plugClass.getMethod("setPrintWay", Consumer.class);
                met.invoke(null, allPrintWay);
                allPrintWay.accept(plugClass.getName() + " : " + "printable");
            } catch (Exception e) {
            }
            try {
                Method met = plugClass.getMethod("setInputWay", Supplier.class);
                met.invoke(null, allInputWay);
                allPrintWay.accept(plugClass.getName() + " : " + "inputable");
            } catch (Exception e) {
            }

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
            ObjectInputStream objin = new ObjectInputStream(cntSot.getInputStream());
            char[] c= new char[10];
            int len = 0;
            while(true)
            {
                AbsDataPack date = (AbsDataPack)objin.readObject();
                if(date.getDataType() == AbsType.CLOSE)
                {
                    close();
                    break;
                }
                ArrayList<? extends AbsClientPlug> ableplugs = PlugProcess.judgeLegalPlugToInst(date, tempPlug);
                if(ableplugs != null)
                {
                    int count = 0;
                    for(var plug : ableplugs)
                    {
                        Consumer<AbsDataPack> printWay = plug.whenReceive(date, this);
                        if(printWay != null)
                        {
                            printWay.accept(date);
                        }
                        else
                        {
                            count++;
                        }
                    }
                    if(ableplugs.size() == count)
                    {
                        allPrintWay.accept(date.toString());
                    }
                }
            }
        } catch (Exception e) {
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
    public Client(String ip)
    {
        this(ip, null, null);
        //默认用console输入输出
    }
    public Client(String ip, Consumer<String> printWay, Supplier<String> inputWay) {
        this(printWay, inputWay);
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
//        if(plugArrs.size() == 0)
//        {
//            plugArrs = matchPlug("chat");
//        }
        if(plugArrs.size() > 1)
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
        ArrayList<AbsClientPlug<?, ?>> enablePlugList = matchPlug(split(str).get(0));
        if(enablePlugList.size() == 0)
        {
            AbsDataPack<String> datepack = new AbsDataPack<>();
            datepack.setDataType(anylizeType(start));
            datepack.setStartWith(start);
            datepack.setData(str);
            allPrintWay.accept("(" + datepack.getDataType() + ")" + name + ":" + str);
//            try {
//                objout.writeObject(datepack);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            return;
        }
        AbsDataPack absDataPack = new AbsDataPack();
        absDataPack.setDataType(anylizeType(start));
        absDataPack.setClientname(name);
        absDataPack.setStartWith(start);
        allPrintWay.accept("(" + absDataPack.getDataType() + ")" + name + ":" + str);
        try
        {
            int localNum = 0;
            boolean isAllInLocal = false;
            /*if(objout == null)
            {
                throw new RuntimeException("连接错误");
            }*/
            int i = 0;
            for(i = 0; i < enablePlugList.size(); i++)
            {
                AbsClientPlug plug = enablePlugList.get(i);
                if(plug.isLocal())
                {
                    localNum++;
                    plug.processPack(null, this);
                }
                else
                {
                    plug.processPack(absDataPack, this);
                }
            }
            isAllInLocal = (i == localNum);
            if(!isAllInLocal)
            {
                objout.writeObject(absDataPack);
            }
            for(i = 0; i < enablePlugList.size(); i++)
            {
                enablePlugList.get(i).afterSend(absDataPack, this);
            }
            tempStatement = null;
        } catch (IOException e) {
            try {
                cntSot.sendUrgentData(0xff);
            } catch (IOException ioException) {
                allPrintWay.accept("服务端崩了，程序尝试重连");
                try {
                    cntSot = new Socket(ip,12221);
                    if(cntSot.isConnected())
                    {
                        objout.close();
                        objout = new ObjectOutputStream(cntSot.getOutputStream());
                        allPrintWay.accept("连接成功");
                    }
                } catch (IOException exception) {
                    System.err.println("连接失败，服务器真的崩了");
                    System.exit(-11);
                }
            }
        }
    }
}
