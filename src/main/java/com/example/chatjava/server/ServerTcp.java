package com.example.chatjava.server;



import com.example.chatjava.logic.ChannelManager;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerTcp {

    public static void main(String[] args) {
        int port = 5566;
        ServerTcp server = new ServerTcp(); // Crea una instancia del servidor
        ChannelManager channelManager = new ChannelManager(server); // Pasa la referencia del servidor al ChannelManager

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                channelManager.add(serverSocket.accept());
                System.out.println("Cliente conectado");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
