package com.first.reflect;

/**
 * @author 原初
 * @create 2021 - 11 - 07
 */
public class ServerRef {
    static boolean isClient(String detialName,String className,String name)
    {
        String temp = "com.first." + detialName + "." + className;
        Class target = null;
        try {
            target = Class.forName(temp);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return false;//没写完
    }
}
