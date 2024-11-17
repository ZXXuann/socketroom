package com.demo;

import java.io.*;
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
//            OutputStreamWriter osw=new OutputStreamWriter(os);
//            BufferedWriter bw=new BufferedWriter(osw);
            PrintWriter pw=new PrintWriter(os);
            System.out.println("请输入内容");

            //创建一个发送数据的线程
            new Thread(()->{
                while(true){
                    Scanner sc=new Scanner(System.in);
                    //读取文字
                    String input=sc.nextLine();
                    //输出文字
                    pw.println(input);
                    pw.flush();


                }
            }).start();

            //创建一个接收数据的线程
            InputStream is=socket.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);
            new Thread(()->{
                while(true){
                    try {
                        String message=br.readLine();
                        System.out.println("收到服务端的消息:"+message);
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }

                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
