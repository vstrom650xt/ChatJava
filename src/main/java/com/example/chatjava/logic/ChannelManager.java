package com.example.chatjava.logic;

import com.example.chatjava.model.Message;
import com.example.chatjava.server.ServerTcp;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChannelManager {

    private ServerTcp server; // Añade una referencia al servidor

    private List<CommunicationManager> communicationManagerList;
    @FXML
    private VBox boxmensa;
    public ChannelManager(ServerTcp server) {
        this.server = server;
        communicationManagerList = new ArrayList<>();
    }
    public void add(Socket socket) {
        CommunicationManager c = new CommunicationManager(socket,this,boxmensa);
        communicationManagerList.add(c);

        Thread thread = new Thread(c);
        thread.setDaemon(true);
        thread.start();
    }

    public void broadcast(Message message, CommunicationManager sender) {
        for (CommunicationManager c : communicationManagerList) {
            if (c != sender) {
                c.send(message);
            }
        }
    }

    public synchronized void remove(CommunicationManager communicationManager) {
        communicationManagerList.remove(communicationManager);
    }
}
