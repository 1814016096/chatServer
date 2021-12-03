package com.first.plug;

import java.io.Serializable;

/**
 * @author 原初
 * @create 2021 - 11 - 08
 * @version 0.0.2 一个枚举类，定义插件的传输数据类型类型
 */
public enum AbsType implements Serializable {
    START,//开始连接

    CORE,//抽象类专用，别用

    INFO,//信息，只限服务端用,如果你的插件只想发送给某个指定客户端信息请用这个

    CHAT,//聊天文本,如果你想要只处理默认的聊天信息请写这个

    COMMAND,//聊天框架命令,如果你想要输入一些命令，就用它了

    PLUG,//自定义插件请用这个

    END,//结束，表示发送结束

    CLOSE;//代表客户端或服务端关闭
    static final long serialVersionUID = 111L;
}
