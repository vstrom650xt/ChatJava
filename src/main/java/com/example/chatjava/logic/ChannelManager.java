package com.example.chatjava.logic;


import com.example.chatjava.model.Message;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChannelManager {
    private List<CommunicationManager> communicationManagerList;

    public ChannelManager() {
        communicationManagerList = new ArrayList<>();
    }

    public void add(Socket socket) {
        CommunicationManager c = new CommunicationManager(socket);
        communicationManagerList.add(c);

        Thread thread = new Thread(c);
        thread.setDaemon(true);
        thread.start();
    }

    public void broadcast(Message message, CommunicationManager sender) {
        Iterator<CommunicationManager> iterator = communicationManagerList.iterator();
        while (iterator.hasNext()) {
            CommunicationManager c = iterator.next();
            if (c != sender) {
                c.send(message);
            }
        }
    }

    public synchronized void remove(CommunicationManager communicationManager) {
        communicationManagerList.remove(communicationManager);
    }
}
