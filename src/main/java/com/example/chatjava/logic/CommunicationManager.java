package com.example.chatjava.logic;

import com.example.chatjava.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CommunicationManager implements Runnable {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ChannelManager channelManager;  // Agregado para la referencia al ChannelManager

    // Modificado para aceptar un ChannelManager como argumento
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
                System.out.println("Received message " + message.getText());

                // Llama al método del ChannelManager para enviar el mensaje a todos los clientes
                channelManager.broadcast(message, this);
            }
        } catch (IOException | ClassNotFoundException e) {
            // Manejar la desconexión del cliente (puedes imprimir un mensaje)
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
}
