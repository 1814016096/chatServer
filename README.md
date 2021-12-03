# chatServer
### 一个实现了插件的基于socket(TCP协议)的开源服务器-客户端程序(目前还未完成)
目前已经写完了，但是文档暂时还未出炉~~(因为懒)~~
#### 说明
>1. 本项目目前已经完成，目前还未写完说明文档
>2. 本项目基于GPL3.0开源
>3. 本人非科班程序员，平时也比较忙，可能更新比较慢
chatServer是我的一个学习了Java socket网络编程和反射之后奇思妙想的一个产物
#### 功能（预期实现）
> * 基于TCP的聊天服务器、客户端
> * 通过反射功能加载插件
> * 一个基于Javafx的漂亮界面（可能会最后实现）
> * 一个插件开发文档(这个可能也需要很久)
#### 实现方式
* 以反射的方式获取jar中的插件，并将其加载至JVM中。  
* 运用回调方法,来实现在接受和发送时通过插件处理数据  
* 数据并非以数据的方式发送，而是以自定义数据包类的实例的方式发送和接受，增强了拓展性
#### main主类
>client的主类是com.first.factory.ClientFactory  
>server的主类是com.first.factory.ServerFactory