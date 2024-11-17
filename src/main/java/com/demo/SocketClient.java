package com.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    public static void main(String[] args) {
        Socket socket=null;
        try {
            //创建客户端socket
            socket=new Socket("127.0.0.1", 8080);
            //获取socket的输出流
            OutputStream os=socket.getOutputStream();
            OutputStreamWriter osw=new OutputStreamWriter(os);

            System.out.println("请输入内容");

            //创建一个发送数据的线程
            new Thread(()->{
                while(true){
                    Scanner sc=new Scanner(System.in);
                    //读取文字
                    String input=sc.nextLine();
                    InputStream inputStream=socket.getInputStream();

                }
            })
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
