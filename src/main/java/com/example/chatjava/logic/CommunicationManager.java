package com.example.chatjava.logic;

import com.example.chatjava.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CommunicationManager implements Runnable {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ChannelManager channelManager;
    public CommunicationManager(Socket socket, ChannelManager channelManager) {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.channelManager = channelManager;  // Inicializa la referencia al ChannelManager
        } catch (IOException e) {
            e.printStackTrace();
            closeResources();
        }
    }

    private void closeResources() {
        try {
            if (oos != null) {
                oos.close();
            }
            if (ois != null) {
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Message message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = (Message) ois.readObject();
                if (message == null) {
                    // El cliente ha cerrado la conexión
                    break;
                }
                System.out.println("Received message " + message.getText());

                // Llama al método del ChannelManager para enviar el mensaje a todos los clientes
                channelManager.broadcast(message, this);
            }
        } catch (IOException | ClassNotFoundException e) {
            // Manejar la desconexión del cliente
            System.err.println("Cliente desconectado: " + e.getMessage());
        } finally {
            // Elimina el CommunicationManager desconectado
            channelManager.remove(this);
            closeResources();
        }
    }

}
