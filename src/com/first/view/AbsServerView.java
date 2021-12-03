package com.first.view;

import com.first.server.ServerManager;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author 原初
 * @create 2021 - 12 - 02
 */
public interface AbsServerView {
    void start();
    void setServer(ServerManager setManager);
    ServerManager getManager();
    Consumer<String> getPrintWay();
    Supplier<String> getInputWay();
}
