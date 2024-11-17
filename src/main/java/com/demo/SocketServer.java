package com.demo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SocketServer {
    public static void main(String[] args) {
        //创建一个服务器Socket
        ServerSocket server=null;
        //将会话保存
        Map<String, Socket> SESSION=new ConcurrentHashMap<>();
        try {
            //开启服务器Socket，端口为8080
            server=new ServerSocket(8080);
            System.out.println("服务器已经启动");
            while(true){
                //接收客户端的socket访问
                Socket socket=server.accept();
                String ip=socket.getInetAddress().getHostAddress();
                int port=socket.getPort();
                System.out.println("有新的客户端加入，ip:"+ip+",端口:"+port);
                //保存客户端会话
                String clientName=ip+":"+socket.getPort();
                SESSION.put(clientName,socket);
                new Thread(()->{
                    while(true){
                        try {
                            InputStream inputStream=socket.getInputStream();
                            //将字节流转换为字符流
                            InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"utf8");
                            //创建缓存区
                            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                            //读取字符
                            String message=bufferedReader.readLine();
                            System.out.println("收到客户端"+socket.getInetAddress().getHostAddress()
                                    +":"+socket.getPort()+"的消息:"+message);
                            //转发消息给所有的客户端
                            for(Map.Entry<String,Socket> entry:SESSION.entrySet()){
                                String key=entry.getKey();
                                System.out.println(key);
                                //获取客户端socket
                                Socket value=entry.getValue();
                                //获取客户端socket的输出流
                                OutputStream outputStream=value.getOutputStream();
                                //将字节流转换为字符流
                                OutputStreamWriter outputStreamWriter=new OutputStreamWriter(outputStream,"utf8");
                                //创建socket输出的缓存区
                                PrintWriter pw=new PrintWriter(outputStream);
                                BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
                                //注意换行符
                                pw.println(socket.getPort()+":"+message);
                                bufferedWriter.write(socket.getPort()+":"+message+"\n");
                                bufferedWriter.flush();
                                //清空缓冲区
                                pw.flush();
                            }

                        } catch (IOException e) {
                            //关闭线程
                            e.printStackTrace();
                            break;
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
