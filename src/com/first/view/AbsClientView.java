package com.first.view;

import com.first.client.Client;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author 原初
 * @create 2021 - 11 - 30
 */
public abstract interface AbsClientView {
    void start();
    void setClient(Client client);
    Client getClient();
    Consumer<String> getPrintWay();
    Supplier<String> getInputWay();
}
