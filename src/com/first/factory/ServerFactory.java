package com.first.factory;

import com.first.server.ServerManager;
import com.first.view.AbsServerView;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * @author 原初
 * @create 2021 - 11 - 30
 * @version 0.1.0 Server工厂类
 */
public class ServerFactory {
    private static int port = 12221;
    private static String clinetPlugDir;
    private static String serverPlugDir;
    private static Class<AbsServerView> startClass;
    static {
        String mainTo = "/chatServe";
        File properFile = new File(System.getProperty("user.dir") + mainTo + "/serverStart.properties");
        try (FileInputStream input = new FileInputStream(properFile)){
            Properties pros = new Properties();
            pros.load(input);
            String clientPlugdir = pros.getProperty("serverclientplugdir");
            String serverPlugdir = pros.getProperty("serverplugdir");
            if(clientPlugdir != null && clientPlugdir.length() != 0)
            {
                ServerFactory.clinetPlugDir = clientPlugdir;
            }
            else
            {
                throw new RuntimeException();
            }
            if(serverPlugdir != null && serverPlugdir.length() != 0)
            {
                ServerFactory.serverPlugDir = serverPlugdir;
            }
            String port = pros.getProperty("port");
            if(port != null && port.length() != 0)
            {
                ServerFactory.port = Integer.parseInt(port);
            }
            Class startClass = Class.forName(pros.getProperty("start"));
            ServerFactory.startClass = startClass;
        } catch (Exception e) {
            System.out.println("配置信息错误");
            System.exit(-1);
        }
    }
    public static synchronized AbsServerView getView() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        AbsServerView currentViwe = null;
        currentViwe = (AbsServerView) startClass.getConstructor().newInstance();
        String mainTo = "/chatServe";
        ServerManager manager = new ServerManager(port, currentViwe.getPrintWay(),
                currentViwe.getInputWay(), clinetPlugDir, serverPlugDir);
        currentViwe.setServer(manager);
        return currentViwe;
    }
    public static void main(String[] args) {
        AbsServerView view = null;
        try {
            view = ServerFactory.getView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.start();

    }
}
