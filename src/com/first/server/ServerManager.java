package com.first.server;

import com.first.factory.ClientFactory;
import com.first.plug.AbsPlug;
import com.first.plug.client.AbsClientPlug;
import com.first.plug.server.AbsServerPlug;
import com.first.plugloader.PlugProcess;
import com.first.view.AbsClientView;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;
/**
 * @author 原初
 * @create 2021 - 11 - 04
 * @version 0.1.0 对核心Sever的管理类
 */
public class ServerManager {
    ArrayList<Class<? extends AbsPlug>> serverPlugsCl;
    ArrayList<AbsServerPlug<?>> serverPlugs = new ArrayList<>(20);
    ArrayList<CoreServer> all;
    ServerSocket serSoc;//服务器socket
    AbsClientView serverClient;
    Map<CoreServer, ObjectOutputStream> allObjOut;
    private String whatCharSet = "gbk";
    private Consumer<String> allPrintWay = System.out::println;
    private Supplier<String> allInputWay = () ->{
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    };
    public ServerManager()
    {
        this(null,null);
    }

    public ServerManager(Consumer<String> allPrintWay, Supplier<String> allInputWay) {
        this(12221,allPrintWay,allInputWay, "serverClientPlug", "serverplug");
    }

    public ServerManager(int ip, Consumer<String> allPrintWay, Supplier<String> allInputWay,
                         String dir, String serverDir) {
        all = new ArrayList<>(20);
        serverPlugsCl = new ArrayList<>(20);
        allObjOut = new HashMap<>();
        String mainTo = "/chatServe";
        File plugDir = new File(System.getProperty("user.dir") + mainTo + "/" + serverDir);
        File[] plugs = plugDir.listFiles();
        for(var plug : plugs)
        {
            if(plug.getName().endsWith(".jar"))
            {
                serverPlugsCl.addAll(PlugProcess.getAllPathPlug(plug.getAbsolutePath(),"ServerPlug"));
            }
        }
        if(allInputWay != null)
        {
            this.allInputWay = allInputWay;
        }
        if(allPrintWay != null)
        {
            this.allPrintWay = allPrintWay;
        }
        for (var plug : serverPlugsCl) {
            try {
                serverPlugs.add((AbsServerPlug<?>) plug.getConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (var plugClass : serverPlugsCl) {
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

            for (var instance : serverPlugs) {
                instance.whenInit(this);
            }
        }
        try {
            serSoc = new ServerSocket(ip);
        } catch (IOException e) {
            allPrintWay.accept(e.getMessage());
        }
        ClientFactory.setPlugDir(dir);
        serverClient = ClientFactory.getView();
        serverClient.start();
    }
    public void addCnt() {
        Socket accept = null;
        try {
            accept = serSoc.accept();
            CoreServer coreServer = new CoreServer(accept, this.serverPlugs, allObjOut);
            coreServer.start();
            all.add(coreServer);
            coreServer.setOthers(all);
            for(var plug : serverPlugs)
            {
                plug.afterReceive(coreServer);
            }
        } catch (IOException e) {
            allPrintWay.accept(e.getMessage());
        }
        String nameStr = null;
    }
}
