package com.example.chatjava.server;



import com.example.chatjava.logic.ChannelManager;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerTcp {

    public static void main(String[] args) {
        int port =5566;
        ChannelManager channelManager = new ChannelManager();

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                channelManager.add(serverSocket.accept());

                System.out.println("cliente conectado  ");

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
