package com.first.factory;

import com.first.client.Client;
import com.first.view.AbsClientView;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author 原初
 * @create 2021 - 11 - 30
 * @version 0.0.2 client工厂类
 */
public class ClientFactory {
    private static String ip = null;
    private static int port = 12221;
    private static Class startClass;
    private static String plugDir = "clientPlug";


    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        ClientFactory.ip = ip;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        ClientFactory.port = port;
    }

    public static Class getStartClass() {
        return startClass;
    }

    public static void setStartClass(Class startClass) {
        ClientFactory.startClass = startClass;
    }

    public static String getPlugDir() {
        return plugDir;
    }

    public static void setPlugDir(String plugDir) {
        ClientFactory.plugDir = plugDir;
    }

    static {
        String mainTo = "/chatServe";
        File properFile = new File(System.getProperty("user.dir") + mainTo + "/clientStart.properties");
        try (FileInputStream input = new FileInputStream(properFile)){
            Properties pros = new Properties();
            pros.load(input);
            String ip = pros.getProperty("ip");
            if(ip == null || ip.length() == 0)
            {
                throw new Exception();
            }
            ClientFactory.ip = ip;
            String plugDir = pros.getProperty("plugdir");
            if(plugDir != null || plugDir.length() != 0)
            {
                ClientFactory.plugDir = plugDir;
            }
            String port = pros.getProperty("port");
            if(port != null && port.length() != 0)
            {
                ClientFactory.port = Integer.parseInt(port);
            }
            Class startClass = Class.forName(pros.getProperty("start"));
            ClientFactory.startClass = startClass;
        } catch (Exception e) {
            System.out.println("配置信息错误");
            System.exit(-1);
        }
    }
    public static synchronized AbsClientView getView()
    {
        AbsClientView currentViwe = null;
        try {
            currentViwe = (AbsClientView) startClass.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String mainTo = "/chatServe";
        Client client = new Client(port, ip, currentViwe.getPrintWay(), currentViwe.getInputWay(), plugDir);
        currentViwe.setClient(client);
        return currentViwe;
    }
    public static void main(String[] args) {
        AbsClientView view = ClientFactory.getView();
        view.start();
    }
}
