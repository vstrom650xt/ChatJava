package com.example.chatjava.logic;

import com.example.chatjava.model.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CommunicationManager implements Runnable {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ChannelManager channelManager;

    @FXML
    private VBox boxmensa;

    public CommunicationManager(Socket socket, ChannelManager channelManager, VBox boxmensa) {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.channelManager = channelManager;
            this.boxmensa = boxmensa;  // Asigna la referencia pasada como parámetro
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

                // Envía el mensaje al VBox de la interfaz gráfica si boxmensa no es nulo
                if (boxmensa != null) {
                    Platform.runLater(() -> {
                        Label label = new Label(message.toString());
                        HBox hBox = new HBox(label);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        boxmensa.getChildren().add(hBox);
                    });
                }

                // Envía el mensaje a todos los clientes conectados
                channelManager.broadcast(message, this);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
}
