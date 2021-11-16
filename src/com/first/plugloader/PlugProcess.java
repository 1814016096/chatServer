package com.first.plugloader;

import com.first.datapack.AbsDataPack;
import com.first.plug.AbsPlug;
import com.first.plug.AbsType;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author 原初
 * @create 2021 - 11 - 10
 * @version 0.0.2 插件的解析、处理、生成插件,包含两个静态方法
 */
public class PlugProcess {
    public ArrayList<Class<? extends AbsPlug<?>>> judgeLegalPlug(AbsDataPack<?> datepack
            , ArrayList<Class<? extends AbsPlug<?>>> plugs) {
        ArrayList<Class<? extends AbsPlug<?>>> target = new ArrayList<>();
        for (var plug : plugs) {
            if (!plug.getName().contains("Abs")) {
                try {
                    AbsPlug<?> serverPlug = plug.newInstance();
                    Method typeMet = null;
                    Method startTo = null;
                    typeMet = plug.getMethod("getCtrlplugType");
                    startTo = plug.getMethod("getCtrlName");
                    if (datepack.getDataType() == (AbsType) typeMet.invoke(serverPlug)) {
                        //要判断三个相等：1.名字 2.类型 3.泛型的类型
                        boolean temp = datepack.getStartWith().equals(startTo.invoke(serverPlug));
                        if(temp)
                        {
                            temp = datepack.getInnerData().getClass().getTypeName()
                                .equals(serverPlug.flag.getClass().getTypeName());
                        }
                        if (temp)
                        {
                            target.add(plug);
                        }
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        if (target.size() != 0) {
            return target;
        } else {
            return null;//已测试
        }
    }



        public ArrayList<Class<? extends AbsPlug<?>>> getAllPathPlug(String jarPathName, String AnnType)
    {
        String urlpath = "file:";
        ArrayList<Class<? extends AbsPlug<?>>> targets = new ArrayList<>();
        URLClassLoader classLoader = null;
        Enumeration<JarEntry> jarEn = null;
        String jar = urlpath + jarPathName;
        JarFile thisJar = null;
        try{
            URL path = new URL(jar);
            thisJar = new JarFile(jarPathName);
            classLoader = new URLClassLoader(new URL[]{path});
            jarEn = thisJar.entries();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(jarEn.hasMoreElements())
        {
            String realName = jarEn.nextElement().getRealName();
            if(realName != null && realName.endsWith(".class"))
            {
                Class<? extends AbsPlug<?>> undeteClass = null;
                try {
                    undeteClass = (Class<? extends AbsPlug<?>>) classLoader.loadClass(realName.replace("/", ".")
                            .replace(".class", ""));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                Annotation[] annotations = undeteClass.getAnnotations();
                for(Annotation temp : annotations) {
                    if (temp.toString().contains(AnnType)) {
                        targets.add(undeteClass);
                    }
                }
            }
        }
        if(targets.size() != 0)
        {
            return targets;
        }
        return null;
    }
}
