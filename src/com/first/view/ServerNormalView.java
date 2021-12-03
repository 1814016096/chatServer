package com.first.view;

import com.first.server.ServerManager;

import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author 原初
 * @create 2021 - 12 - 02
 */
public class ServerNormalView implements AbsServerView{
    ServerManager manager;
    Scanner scanner = new Scanner(System.in);
    @Override
    public void start() {
        while (true)
        {
            manager.addCnt();
        }
    }

    @Override
    public void setServer(ServerManager setManager) {
        this.manager = setManager;
    }

    @Override
    public ServerManager getManager() {
        return manager;
    }

    @Override
    public Consumer<String> getPrintWay() {
        return System.out::println;
    }

    @Override
    public Supplier<String> getInputWay() {
        return scanner::nextLine;
    }
}
